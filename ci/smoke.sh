#!/usr/bin/env bash
# Димовий тест: сервер (дані, світогенерація, структури) і клієнт (моделі, текстури, частинки) з модом.
set -u
mkdir -p run
echo "eula=true" > run/eula.txt
printf "online-mode=false\nspawn-protection=0\nlevel-seed=pletinnya\n" > run/server.properties

echo "=== SERVER ==="
( sleep 150
  for s in mage_tower ley_circle shrine aurora_hut academy_ruins dragon_sanctuary spirit_grove weaver_vault; do
    echo "locate structure pletinnya:$s"; sleep 4
  done
  echo "stop" ) | timeout 420 ./gradlew runServer --console=plain > server_out.txt 2>&1 || true
grep -E "Done \(|ERROR|Exception|Couldn't|Failed|Unknown|located|could not find|nearest" -i run/logs/latest.log | grep -vE "realms|telemetry|Unable to load the Realms" | head -80

echo "=== CLIENT ==="
rm -f run/logs/latest.log
timeout 200 xvfb-run -a -s "-screen 0 1280x720x24" ./gradlew runClient --console=plain > client_out.txt 2>&1 || true
grep -E "ERROR|Exception|Missing|Unable to|Failed|missing" run/logs/latest.log | grep -vE "realms|telemetry|Realms|narrator|OpenAL|joystick|Narrator|GLFW error 65544" | sort | uniq -c | sort -rn | head -60
grep -c "Плетіння пробуджується" run/logs/latest.log || true
