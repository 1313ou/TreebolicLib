#!/bin/bash

thisdir="`dirname $(readlink -m $0)`"
thisdir="$(readlink -m ${thisdir})"
dirres=../src/main/res
dirassets=../src/main/assets
dirapp=..

declare -A popup_res
popup_res=([mdpi]=32 [hdpi]=48 [xhdpi]=64 [xxhdpi]=96 [xxxhdpi]=128)
popup_list="menu_*.svg"

declare -A toolbar_res
#toolbar_res=([mdpi]=20 [hdpi]=30 [xhdpi]=40 [xxhdpi]=60)
toolbar_res=([mdpi]=32 [hdpi]=48 [xhdpi]=64 [xxhdpi]=96 [xxxhdpi]=128)
toolbar_list="toolbar_*.svg"

declare -A status_res
status_res=([mdpi]=32 [hdpi]=48 [xhdpi]=64 [xxhdpi]=96 [xxxhdpi]=128)
status_list="status_*.svg"

declare -A splash_res
splash_res=([mdpi]=144 [hdpi]=192 [xhdpi]=288 [xxhdpi]=384 [xxxhdpi]=576)
splash_list="splash_*.svg"

# menu
for svg in ${popup_list}; do
	for r in ${!popup_res[@]}; do 
		d="${dirres}/drawable-${r}"
		mkdir -p ${d}
		png="${svg%.svg}.png"
		echo "${svg}.svg -> ${d}/${png} @ resolution ${popup_res[$r]}"
		inkscape ${svg} --export-png=${d}/${png} -h${popup_res[$r]} > /dev/null 2> /dev/null
	done
done

# toolbar
for svg in ${toolbar_list}; do
	for r in ${!toolbar_res[@]}; do 
		d="${dirres}/drawable-${r}"
		mkdir -p ${d}
		png="${svg%.svg}.png"
		echo "${svg}.svg -> ${d}/${png} @ resolution ${toolbar_res[$r]}"
		inkscape ${svg} --export-png=${d}/${png} -h${toolbar_res[$r]} > /dev/null 2> /dev/null
	done
done

# status
for svg in ${status_list}; do
	for r in ${!status_res[@]}; do 
		d="${dirres}/drawable-${r}"
		mkdir -p ${d}
		png="${svg%.svg}.png"
		echo "${svg}.svg -> ${d}/${png} @ resolution ${status_res[$r]}"
		inkscape ${svg} --export-png=${d}/${png} -h${status_res[$r]} > /dev/null 2> /dev/null
	done
done

# splash
for svg in ${splash_list}; do
	for r in ${!splash_res[@]}; do 
		d="${dirres}/drawable-${r}"
		mkdir -p ${d}
		png="${svg%.svg}.png"
		echo "${svg}.svg -> ${d}/${png} @ resolution ${splash_res[$r]}"
		inkscape ${svg} --export-png=${d}/${png} -h${splash_res[$r]} > /dev/null 2> /dev/null
	done
done

