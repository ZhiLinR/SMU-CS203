#!sh
# Hotfix from Issue #30 on GitHub
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_VALUE_1=""

# Check if .gitignore is modified and staged

if git diff --merge-base main --name-only --diff-filter=MD | grep -i '.gitignore'; then
  set -x
  echo 'Backend/.gitignore modifications or deletions are not allowed! Fixing...'
  set +x
  git checkout origin/main Backend/.gitignore
  exit 0
fi