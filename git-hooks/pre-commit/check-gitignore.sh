#!sh
# Check if .gitignore is modified and staged
export GIT_CONFIG_KEY_0="user.name"
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_KEY_1="user.email"
export GIT_CONFIG_VALUE_1=""

if git diff --merge-base main --name-only | grep -i '.gitignore'; then
  echo '.gitignore changes are not allowed! Fixing...'
  git checkout origin/main Backend/.gitignore
  exit 0
fi
