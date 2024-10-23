#!sh
#!/bin/sh
# Check if .gitignore is modified and staged
export GIT_CONFIG_KEY_0="user.name"
export GIT_CONFIG_VALUE_0=""
export GIT_CONFIG_KEY_1="user.email"
export GIT_CONFIG_VALUE_1=""

if git diff --name-only --cached --diff-filter=AM | grep -i '.env'; then
  echo 'Do not include files with credentials.'
  exit 1
fi

exit 0