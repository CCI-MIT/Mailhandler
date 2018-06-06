#!/bin/bash

eval "$(ssh-agent -s)"
chmod 600 /tmp/deploy_rsa
ssh-add /tmp/deploy_rsa

DEPLOY_SERVER=binaries@cognosis2.mit.edu
echo "Copying binaries to ${DEPLOY_SERVER}:mailhandler ..."
scp target/mailhandler-*.jar ${DEPLOY_SERVER}:mailhandler/

# Workaround for hanging builds
# taken from https://github.com/travis-ci/travis-ci/issues/8082#issuecomment-315147561
ssh-agent -k
