#!/bin/bash

source "../../../make-artwork-lib.sh"

toolbar_list="toolbar_*.svg"
status_list="status_*.svg"
menu_list="menu_*.svg"
splash_list="splash.svg"

make_res "${toolbar_list}" 32
make_res "${status_list}" 32
make_res "${menu_list}" 32
make_res "${splash_list}" 144

