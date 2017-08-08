#!/bin/bash

eval "$(ssh-agent -s)"
ssh-add deploy_rsa

DEPLOY_SERVER=travis@cognosis.mit.edu
echo "Copying binaries to ${DEPLOY_SERVER}:mailhandler ..."
scp target/mailhandler-*.jar ${DEPLOY_SERVER}:mailhandler/

# Workaround for hanging builds
# taken from https://github.com/travis-ci/travis-ci/issues/8082#issuecomment-315147561
ssh-agent -k