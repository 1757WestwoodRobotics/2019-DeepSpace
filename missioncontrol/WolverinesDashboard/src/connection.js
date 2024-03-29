const exec = require('child_process').exec;
const fixPath = require('fix-path');
const electron = require('electron');

var sudo = require('sudo-prompt');
var options = {
    name: 'Electron',
    icns: '/Applications/Electron.app/Contents/Resources/Electron.icns', // (optional)
};

let address = document.getElementById('connect-address'),
    connect = document.getElementById('connect'),
    buttonConnect = document.getElementById('connect-button');

let pit_if = document.getElementById('ds_pit_ifconfig');
let field_if = document.getElementById('ds_field_ifconfig');
let dhcp_if = document.getElementById('ds_dhcp_ifconfig');
let dev_if = document.getElementById('dev_pit_ifconfig');

let loginShown = true;


pit_if.onclick = () => {
    fixPath();
    // Windows
    if (process.platform === 'win32') {

        sudo.exec('netsh interface ipv4 set address name="Ethernet" static 10.17.57.5 255.255.255.0 10.17.57.1', options,
            function (error, stdout, stderr) {
                if (error) throw error;
                console.log('stdout: ' + stdout);
            });

        alert("Changing IP settings to PIT configuration! Launching ADMIN UAC");
    }
};

field_if.onclick = () => {
    fixPath();
    // Windows
    if (process.platform === 'win32') {

        sudo.exec('netsh interface ipv4 set address name="Ethernet" static 10.17.57.5 255.0.0.0 10.17.57.1', options,
            function (error, stdout, stderr) {
                if (error) throw error;
                console.log('stdout: ' + stdout);
            });

        alert("Changing IP settings to FIELD configuration! Launching ADMIN UAC");
    }
};

dhcp_if.onclick = () => {
    fixPath();
    // Windows
    if (process.platform === 'win32') {

        sudo.exec('netsh interface ipv4 set address name="Ethernet" dhcp', options,
            function (error, stdout, stderr) {
                if (error) throw error;
                console.log('stdout: ' + stdout);
            });

        alert("Changing IP settings to DHCP configuration! Launching ADMIN UAC");
    }
};

dev_if.onclick = () => {
    fixPath();
    // Windows
    if (process.platform === 'win32') {

        sudo.exec('netsh interface ipv4 set address name="Ethernet" static 10.17.57.50 255.255.255.0 10.17.57.1', options,
            function (error, stdout, stderr) {
                if (error) throw error;
                console.log('stdout: ' + stdout);
            });

        alert("Changing IP settings to DEVELOPER configuration! Launching ADMIN UAC");
    }
};

// Set function to be called on NetworkTables connect. Not implemented.
//NetworkTables.addWsConnectionListener(onNetworkTablesConnection, true);

// Set function to be called when robot dis/connects
NetworkTables.addRobotConnectionListener(onRobotConnection, false);

// Sets function to be called when any NetworkTables key/value changes
//NetworkTables.addGlobalListener(onValueChanged, true);

// Function for hiding the connect box
onkeydown = key => {
    if (key.key === 'Escape') {
        document.body.classList.toggle('login', false);
        loginShown = false;
    }
};

/**
 * Function to be called when robot connects
 * @param {boolean} connected
 */
function onRobotConnection(connected) {
    var state = connected ? 'Robot connected!' : 'Robot disconnected.';
    console.log(state);
    ui.robotState.textContent = state;

    buttonConnect.onclick = () => {
        document.body.classList.toggle('login', true);
        loginShown = true;
    };
    if (connected) {
        // On connect hide the connect popup
        document.body.classList.toggle('login', false);
        loginShown = false;
    } else if (loginShown) {
        setLogin();
    }
}
function setLogin() {
    // Add Enter key handler
    // Enable the input and the button
    address.disabled = connect.disabled = false;
    connect.textContent = 'Connect';
    // Add the default address and select xxxx
    address.value = '10.17.57.2';
    address.focus();
}
// On click try to connect and disable the input and the button
connect.onclick = () => {
    ipc.send('connect', address.value);
    address.disabled = connect.disabled = true;
    connect.textContent = 'Connecting...';
};
address.onkeydown = ev => {
    if (ev.key === 'Enter') {
        connect.click();
        ev.preventDefault();
        ev.stopPropagation();
    }
};

// Show login when starting
document.body.classList.toggle('login', true);
setLogin();
