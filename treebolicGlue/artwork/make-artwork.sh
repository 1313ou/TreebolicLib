#!/bin/bash

source "../../../make-artwork-lib.sh"

toolbar="toolbar_*.svg"
status="status_*.svg"
menu="menu_*.svg"
splash="splash.svg"
progress="progress_*.svg"

make_res "${toolbar}" 32
make_res "${status}" 32
make_res "${menu}" 32
make_res "${progress}" 144
make_res "${splash}" 144

