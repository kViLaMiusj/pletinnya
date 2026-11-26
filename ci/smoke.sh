#!/usr/bin/env bash
# Димовий тест: сервер (дані, світогенерація, структури), потім клієнт у світі з автоматичними знімками.
set -u
mkdir -p run
echo "eula=true" > run/eula.txt
printf "online-mode=false\nspawn-protection=0\nlevel-seed=pletinnya\nlevel-name=world\n" > run/server.properties

echo "=== SERVER ==="
( sleep 150
  for s in mage_tower ley_circle shrine aurora_hut academy_ruins dragon_sanctuary spirit_grove weaver_vault fire_forge frost_spire storm_beacon sun_obelisk shadow_crypt blood_altar wanderer_camp resonance_amphitheater wind_aerie tide_temple; do
    echo "locate structure pletinnya:$s"; sleep 4
  done
  echo "stop" ) | timeout 420 ./gradlew runServer --console=plain > server_out.txt 2>&1 || true
grep -E "Done \(|ERROR|Exception|Couldn't|Failed|Unknown|nearest|could not" -i run/logs/latest.log | grep -vE "realms|telemetry|data fixer" | head -60

echo "=== CLIENT ==="
mkdir -p run/saves && rm -rf run/saves/smoke && cp -r run/world run/saves/smoke
cat > run/options.txt <<'OPT'
pauseOnLostFocus:false
renderDistance:8
simulationDistance:8
guiScale:2
onboardAccessibility:false
tutorialStep:none
skipMultiplayerWarning:true
joinedFirstServer:true
narrator:0
soundCategory_master:0.0
maxFps:60
graphicsMode:1
OPT
rm -f run/logs/latest.log; rm -rf run/screenshots
PLETINNYA_AUTOTEST=1 timeout 1500 xvfb-run -a -s "-screen 0 1280x720x24" ./gradlew runClient --console=plain > client_out.txt 2>&1 || true
grep -E "ERROR|Exception|Missing|Unable to|Failed|missing|AutoTest" run/logs/latest.log | grep -vE "realms|telemetry|Realms|narrator|OpenAL|Narrator|GLFW error 65544|SoundSystem|flite|data fixer|goat" | sort | uniq -c | sort -rn | head -60
grep -A30 "Exception" run/logs/latest.log | grep -vE "flite|libflite" | head -60
ls run/screenshots | wc -l
