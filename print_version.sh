#!/bin/sh

# Prints current version for application
COMMITS=$(git rev-list --count HEAD)
COMMIT_COUNT=$((700 + $COMMITS))
echo "5.1.$COMMIT_COUNT"
