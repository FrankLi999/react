## set up workspace
cd front-end-bun
bun init
name: hawtio-frent-end
remove index.ts

add workspaces to package.json

## set up packages

### utils

bun create vite utils
update package.json, eslint, prettier, style lint, vitest

### ajv
bun create vite ajv
update package.json, eslint, prettier, style lint, vitest

### core
bun create vite core
update package.json, eslint, prettier, style lint, vitest

### bootstrap5
bun create vite bootstrap5
update package.json, eslint, prettier, style lint, vitest

### patternfly
bun create vite patternfly
update package.json, eslint, prettier, style lint, vitest

### sample project
from project root
bun create vite sample
### console-ui
from project root
bun create vite console-ui
### console-ui-vite 
from project root
bun create vite console-ui-vite
## install
Once added, run bun install from the project root to install dependencies for all workspaces.
To add npm dependencies to a particular workspace, just cd to the appropriate directory and run bun add commands as you would normally. Bun will detect that you are in a workspace and hoist the dependency as needed.

bun install  