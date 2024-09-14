#!/usr/bin/env bash
set -euo pipefail

echo "Deploying..."
aws cloudformation deploy \
--template-file ./ops/deployment/packaged-template.yaml \
--stack-name CheepHonkResources \
--parameter-overrides \
    GitSHA=${GITHUB_SHA:0:8} \
    GoogleApiKey=${GOOGLE_API_KEY} \
    ProxyApiKey=${PROXY_API_KEY} \
    TelegramAuthToken=${TELEGRAM_AUTH_TOKEN} \
    TelegramChatId=${TELEGRAM_CHAT_ID} \
    OriginAddress=${ORIGIN_ADDRESS} \
--capabilities CAPABILITY_IAM
echo "Done!"
