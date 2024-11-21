#!sh
# Hotfix from Issue #30 on GitHub
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_VALUE_1=""

# Check if .gitignore is modified and staged
output=$(git diff --name-only HEAD | grep -i -P '\.env(?!\.example$).*')

if [ -n "$output" ]; then
  set -x # Enables output of commands again
  echo "Do not include files with credentials. Violating file: $output"
  set +x
  exit 1
fi

exit 0