#!/bin/bash
# ./base/reset-image-version.sh v20230606-1 ./overlays/sandbox/kustomization.yaml 
sed -ri 's/^(\s*)(newTag\s*:\s*.*$)/\1newTag: '\"$1\"'/' $2