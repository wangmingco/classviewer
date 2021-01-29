#!/usr/bin/env bash

source ./build.sh

cp ./bin/AppIcon.png ./target
cd ./target

sips -z 100 100 -p 150 150 AppIcon.png --out AppIcon-background.png

mkdir AppIcon.iconset

sips -z 128 128 AppIcon.png --out AppIcon.iconset/icon_128x128.png

iconutil --convert icns AppIcon.iconset

javapackager -deploy -native pkg -name ClassViewer -BappVersion=1.0 -Bicon=package/macosx/AppIcon.icns \
  -srcdir . -srcfiles classviewer-1.0-jar-with-dependencies.jar \
  -appclass dev.wangming.classviewer.ui.Main \
  -outdir . -outfile classviewer
