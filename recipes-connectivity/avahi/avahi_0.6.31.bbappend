# bbappend for avahi

SRC_URI += "file://avahi-conf.patch;patch=1"

FILESEXTRAPATHS := "${THISDIR}/${PN}"

PRINC = "1"
