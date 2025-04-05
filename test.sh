curl -X POST \
  'http://localhost:8080/api/votes' \
  -H 'Content-Type: application/json' \
  -d '{
    "voterAdmissionNumber": "EB1/75122/24",
    "candidateAdmissionNumber": "EB1/12345/24",
    "votingCode": "ABC123"
  }'