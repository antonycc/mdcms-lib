#!/usr/bin/env bash
# Usage: ./generate-jwt.sh > './jwt.id_rsa256_openssl.token'; jwt=$(cat './jwt.id_rsa256_openssl.token') ; echo "[${jwt?}]"
# Warning: Tokens expire in 3019
# Credit: https://stackoverflow.com/questions/58313106/create-rs256-jwt-in-bash
kid_prefix='urn:diyaccounting.co.uk:classpath:/'
signing_key='id_rsa256_openssl'
api_pattern='/my-api/.*'
pem=$(cat ${signing_key?}.pem)
iss='https://diyaccounting.co.uk/'
aud="${iss?}"
now=$(date +%s)
iat="${now?}"
exp=$((${now?} + 60 * 60 * 24 * 365 * 999))
header_raw='{"typ":"JWT","alg":"RS256","kid":"'"${kid_prefix?}${signing_key?}"'.der.pub"}'
header=$(echo -n "${header_raw?}" \
  | openssl base64 \
  | tr -d '=' \
  | tr '/+' '_-' \
  | tr -d '\n')
custom_claims='"'"${iss?}"'":["'"${api_pattern?}"'"]'
payload_raw='{"iat":'"${iat?}"',"exp":'"${exp?}"',"iss":"'"${iss?}"'","aud":"'"${aud?}"'",'"${custom_claims}"'}'
#echo "Payload: ${payload_raw?}"
payload=$(echo -n "${payload_raw?}" \
  | openssl base64 \
  | tr -d '=' \
  | tr '/+' '_-' \
  | tr -d '\n')
unsigned_jwt="${header}"."${payload?}"
signature=$(openssl dgst -sha256 -sign <(echo -n "${pem?}") <(echo -n "${unsigned_jwt?}") \
  | openssl base64 \
  | tr -d '=' \
  | tr '/+' '_-' \
  | tr -d '\n')
echo -n "${unsigned_jwt}"."${signature}"
