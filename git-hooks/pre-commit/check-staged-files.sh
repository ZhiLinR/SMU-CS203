#!sh
#!/bin/sh
# Check if .gitignore is modified and staged
export GIT_CONFIG_KEY_0="user.name"
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_KEY_1="user.email"
export GIT_CONFIG_VALUE_1=""

output=$(git diff --name-only --cached --diff-filter=AM | grep -P '^\.env(?!\.example$).*')

if [ -n "$output" ]; then
  echo "Do not include files with credentials. Violating file: $output"
  exit 1
fi

exit 0