#!/bin/bash

set -e

echo "=== Удаление Kubernetes манифестов для Task Tracker ==="

kubectl delete deployment --all
kubectl delete statefulset --all
kubectl delete svc --all
kubectl delete secret --all
kubectl delete configmap --all
kubectl delete pvc --all

echo "Удаление завершено"
