#!/bin/zsh

set -uo pipefail
setopt null_glob

SCRIPT_DIR=$(cd -- "$(dirname -- "$0")" && pwd)
SCRIPT_NAME=$(basename "$0")
WORKFLOW_FILE="android.yml"
FORCED_BRANCH=""
SHOW_FAILURE_LOGS=false
REQUESTED_LIBRARIES=()
LIBRARIES=(
  renetik-android-framework
)

usage() {
  cat <<EOF
Usage: $SCRIPT_NAME [--branch branch] [library ...]

Checks the latest GitHub Actions run for the selected Renetik Android libraries.
When no libraries are passed, checks the default library list in this script.

Options:
  --branch branch   Check this branch for every repository instead of reading
                    the branch from each workflow file.
  --logs            Show failed step logs for completed unsuccessful runs.
  --failure-logs    Same as --logs.
  -h, --help        Show this help.

Examples:
  $SCRIPT_NAME
  $SCRIPT_NAME renetik-android-core renetik-android-event
  $SCRIPT_NAME --branch master renetik-android-core renetik-android-event
  $SCRIPT_NAME --logs renetik-android-core
EOF
}

require_command() {
  local command_name="$1"
  if ! command -v "$command_name" >/dev/null 2>&1; then
    echo "Missing required command: $command_name" >&2
    exit 2
  fi
}

github_repo_slug() {
  local repo_path="$1"
  local url

  url=$(git -C "$repo_path" remote get-url origin 2>/dev/null || true)
  url="${url%.git}"

  case "$url" in
    git@github.com:*)
      echo "${url#git@github.com:}"
      ;;
    https://github.com/*)
      echo "${url#https://github.com/}"
      ;;
    ssh://git@github.com/*)
      echo "${url#ssh://git@github.com/}"
      ;;
    *)
      return 1
      ;;
  esac
}

workflow_branch() {
  local workflow_path="$1"
  local repo_path="$2"
  local branch

  branch=$(awk '
    function clean(value) {
      gsub(/["[:space:]]/, "", value)
      gsub(/\047/, "", value)
      return value
    }

    /^[[:space:]]*pull_request:/ {
      in_push = 0
      in_branches = 0
    }

    /^[[:space:]]*push:/ {
      in_push = 1
      next
    }

    in_push && /^[[:space:]]*branches:[[:space:]]*\[/ {
      line = $0
      sub(/.*\[/, "", line)
      sub(/\].*/, "", line)
      split(line, branches, ",")
      print clean(branches[1])
      exit
    }

    in_push && /^[[:space:]]*branches:[[:space:]]*$/ {
      in_branches = 1
      next
    }

    in_push && in_branches && /^[[:space:]]*-[[:space:]]*/ {
      line = $0
      sub(/^[[:space:]]*-[[:space:]]*/, "", line)
      print clean(line)
      exit
    }
  ' "$workflow_path")

  if [[ -n "$branch" ]]; then
    echo "$branch"
    return
  fi

  branch=$(git -C "$repo_path" symbolic-ref --quiet --short refs/remotes/origin/HEAD 2>/dev/null || true)
  branch="${branch#origin/}"

  if [[ -n "$branch" ]]; then
    echo "$branch"
    return
  fi

  git -C "$repo_path" symbolic-ref --quiet --short HEAD 2>/dev/null || echo "master"
}

show_failure_logs() {
  local repo_name="$1"
  local repo_slug="$2"
  local run_id="$3"
  local logs

  printf "\n---- Failed logs for %s run %s ----\n" "$repo_name" "$run_id"
  logs=$(gh run view "$run_id" \
    --repo "$repo_slug" \
    --log-failed 2>&1)

  if [[ $? -ne 0 ]]; then
    printf "Unable to read failed logs for %s: %s\n" "$repo_name" "$logs"
  elif [[ -z "$logs" ]]; then
    printf "No failed step logs returned for %s.\n" "$repo_name"
  else
    printf "%s\n" "$logs"
  fi

  printf "---- End failed logs for %s run %s ----\n\n" "$repo_name" "$run_id"
}

check_library_workflow() {
  local repo_name="$1"
  local repo_path="$SCRIPT_DIR/$repo_name"
  local workflow_path="$repo_path/.github/workflows/$WORKFLOW_FILE"
  local repo_slug
  local branch
  local run_info
  local run_id run_status conclusion head_branch head_sha created_at url title

  if [[ ! -d "$repo_path" ]]; then
    printf "FAIL %-32s missing repository directory\n" "$repo_name"
    return 1
  fi

  if [[ ! -f "$workflow_path" ]]; then
    printf "FAIL %-32s missing .github/workflows/%s\n" "$repo_name" "$WORKFLOW_FILE"
    return 1
  fi

  repo_slug=$(github_repo_slug "$repo_path") || {
    printf "FAIL %-32s cannot read GitHub origin remote\n" "$repo_name"
    return 1
  }

  if [[ -n "$FORCED_BRANCH" ]]; then
    branch="$FORCED_BRANCH"
  else
    branch=$(workflow_branch "$workflow_path" "$repo_path")
  fi

  run_info=$(gh run list \
    --repo "$repo_slug" \
    --workflow "$WORKFLOW_FILE" \
    --branch "$branch" \
    --limit 1 \
    --json databaseId,status,conclusion,headBranch,headSha,createdAt,url,displayTitle \
    --jq '.[:1][] | [.databaseId, .status, (.conclusion // ""), .headBranch, .headSha, .createdAt, .url, .displayTitle] | @tsv' 2>&1)

  if [[ $? -ne 0 ]]; then
    printf "FAIL %-32s %-10s gh error: %s\n" "$repo_name" "$branch" "$run_info"
    return 1
  fi

  if [[ -z "$run_info" ]]; then
    printf "FAIL %-32s %-10s no %s runs found\n" "$repo_name" "$branch" "$WORKFLOW_FILE"
    return 1
  fi

  IFS=$'\t' read -r run_id run_status conclusion head_branch head_sha created_at url title <<< "$run_info"

  if [[ "$run_status" == "completed" && "$conclusion" == "success" ]]; then
    printf "OK   %-32s %-10s success    %s %s\n" "$repo_name" "$branch" "$created_at" "$url"
    return 0
  fi

  if [[ "$run_status" == "completed" ]]; then
    printf "FAIL %-32s %-10s %-10s %s %s\n" "$repo_name" "$branch" "$conclusion" "$created_at" "$url"
    if [[ "$SHOW_FAILURE_LOGS" == true ]]; then
      show_failure_logs "$repo_name" "$repo_slug" "$run_id"
    fi
  else
    printf "WAIT %-32s %-10s %-10s %s %s\n" "$repo_name" "$branch" "$run_status" "$created_at" "$url"
  fi

  return 1
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --branch)
      if [[ $# -lt 2 || -z "$2" ]]; then
        echo "--branch requires a branch name" >&2
        exit 2
      fi
      FORCED_BRANCH="$2"
      shift 2
      ;;
    --logs|--failure-logs)
      SHOW_FAILURE_LOGS=true
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    --)
      shift
      REQUESTED_LIBRARIES+=("$@")
      break
      ;;
    -*)
      echo "Unknown option: $1" >&2
      usage >&2
      exit 2
      ;;
    *)
      REQUESTED_LIBRARIES+=("$1")
      shift
      ;;
  esac
done

if [[ ${#REQUESTED_LIBRARIES[@]} -gt 0 ]]; then
  LIBRARIES=("${REQUESTED_LIBRARIES[@]}")
fi

require_command git
require_command gh

echo "Checking GitHub Actions $WORKFLOW_FILE latest runs for ${#LIBRARIES[@]} Renetik Android libraries..."

failure_count=0
for library in "${LIBRARIES[@]}"; do
  if ! check_library_workflow "$library"; then
    failure_count=$((failure_count + 1))
  fi
done

if [[ "$failure_count" -eq 0 ]]; then
  echo "All $WORKFLOW_FILE build checks are successful."
  exit 0
fi

echo "$failure_count $WORKFLOW_FILE build check(s) are not successful."
exit 1
