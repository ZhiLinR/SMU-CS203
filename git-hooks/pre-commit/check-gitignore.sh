#!sh
# Check if .gitignore is modified and staged
export GIT_CONFIG_KEY_0="user.name"
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_KEY_1="user.email"
export GIT_CONFIG_VALUE_1=""

if git diff --merge-base main --name-only --diff-filter=MD | grep -i '.gitignore'; then
  echo 'Backend/.gitignore modifications or deletions are not allowed! Fixing...'
  git checkout origin/main Backend/.gitignore
  exit 0
fi