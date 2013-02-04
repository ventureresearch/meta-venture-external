DESCRIPTION = "NetworkManager"
SECTION = "net/misc"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbffd568227ada506640fe950a4823b"

PR = "r6"

DEPENDS = "libnl dbus dbus-glib udev wireless-tools polkit gnutls util-linux ppp modemmanager"
inherit gnome gettext update-rc.d

RCONFLICTS = "busybox-udhcpc busybox-udhcpd"

INITSCRIPT_NAME = "NetworkManager"
INITSCRIPT_PARAMS = "defaults 85"

SRCREV="750147f94db64e724b7a01a5831831bb28a47157"

SRC_URI = "git://anongit.freedesktop.org/NetworkManager/NetworkManager.git \
    file://gtk-doc.make \
    file://busybox-init-functions.patch;patch=1 \
"

S = "${WORKDIR}/git"

EXTRA_OECONF = " \
		--with-crypto=gnutls \
		--disable-more-warnings \
        --with-dhclient=${base_sbindir}/dhclient \
        --with-iptables=${sbindir}/iptables \
        --enable-ppp \
	--with-modem-manager-1 \
	--enable-ifupdown \
	--disable-ifcfg-rh \
	--disable-ifcfg-suse \
	--disable-ifnet \
	--with-netconfig=no \
"

do_configure_prepend() {

#    http://comments.gmane.org/gmane.comp.handhelds.openembedded/31553
    autopoint || touch config.rpath

    cp ${WORKDIR}/gtk-doc.make ${S}/
    echo "EXTRA_DIST = version.xml" > gnome-doc-utils.make
    sed -i -e s:docs:\:g ${S}/Makefile.am
    sed -i -e /^docs/d ${S}/configure.ac
}

# Work around dbus permission problems since we lack a proper at_console
do_install_prepend() {
	sed -i -e s:deny:allow:g ${S}/src/org.freedesktop.NetworkManager.conf
	sed -i -e s:deny:allow:g ${S}/system-settings/src/nm-system-settings.conf || true
	sed -i -e s:deny:allow:g ${S}/callouts/nm-dispatcher.conf
}

do_install_append () {
	install -d ${D}/etc/dbus-1/event.d

	install -d ${D}/etc/init.d/
	install -m 0755 ${S}/initscript/Debian/NetworkManager ${D}/etc/init.d/NetworkManager

	# Test binaries
	install -d ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/nm-tool ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/libnm* ${D}/usr/bin
	install -m 0755 ${S}/test/.libs/nm-online ${D}/usr/bin

	install -d ${D}/etc/NetworkManager/

	install -m 0600 ${S}/src/org.freedesktop.NetworkManager.conf ${D}/etc/NetworkManager/

	# Install an empty VPN folder as nm-connection-editor will happily segfault without it :o.
	# With or without VPN support built in ;).
	install -d ${D}/etc/NetworkManager/VPN
}

PACKAGES =+ "libnmutil libnmglib libnmglib-vpn ${PN}-tests" 

FILES_libnmutil += "${libdir}/libnm-util.so.*"
FILES_libnmglib += "${libdir}/libnm_glib.so.*"
FILES_libnmglib-vpn += "${libdir}/libnm_glib_vpn.so.*"

systemd_unitdir = "${base_libdir}/systemd/system"

FILES_${PN} += " \
		${libexecdir} \
		${libdir}/pppd/*/nm-pppd-plugin.so \
		${libdir}/NetworkManager/*.so \
		${datadir}/polkit-1 \
		${datadir}/dbus-1 \
		${base_libdir}/udev/* \
"
#
#${systemd_unitdir} \

RRECOMMENDS_${PN} += "iptables"
RCONFLICTS_${PN} = "connman"
RDEPENDS_${PN} = "wpa-supplicant dhcp-client \
           ${@base_contains('COMBINED_FEATURES', '3gmodem', 'ppp', '', d)} \
           "

FILES_${PN}-dbg += "${libdir}/NetworkManager/.debug/ \
		    ${libdir}/pppd/*/.debug/ "

FILES_${PN}-dev += "${datadir}/NetworkManager/gdb-cmd \
                    ${libdir}/pppd/*/*.a \
                    ${libdir}/pppd/*/*.la \
                    ${libdir}/NetworkManager/*.a \
                    ${libdir}/NetworkManager/*.la"

FILES_${PN}-tests = "${bindir}/nm-tool \
                     ${bindir}/libnm_glib_test \
                     ${bindir}/nminfotest \
                     ${bindir}/nm-online \
                     ${bindir}/nm-supplicant \
                     ${bindir}/nm-testdevices"


pkg_postinst_${PN}() {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    
    /etc/init.d/NetworkManager start
}
#systemctl enable NetworkManager.service

pkg_prerm_${PN}() {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi

    /etc/init.d/NetworkManager stop
}
#systemctl disable NetworkManager.service
