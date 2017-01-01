#!/usr/bin/sh
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/test.csv > ./dat/test_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/crypto.csv > ./dat/crypto_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/diy.csv > ./dat/diy_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/travel.csv > ./dat/travel_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/biology.csv > ./dat/biology_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/robotics.csv > ./dat/robotics_clean.csv 
gawk -v RS='"' 'NR % 2 == 0 { gsub(/\n/, "") } { printf("%s%s", $0, RT) }' ./dat/cooking.csv > ./dat/cooking_clean.csv 