@echo off
title rs2hd package manager starting...
start javaw -cp bin;deps/xstream-1.3.1-20081003.103259-2.jar;deps/xpp3-1.1.4c.jar com.rs2hd.tools.PackageManager http://www.grahamedgecombe.com/packages/rs2hd/
