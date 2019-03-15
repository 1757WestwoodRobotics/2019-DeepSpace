//import { setupMaster } from "cluster";

// Define UI elements
let ui = {
    timer: document.getElementById('timer'),
    robotState: document.getElementById('robot-state').firstChild,
    gyro: {
        container: document.getElementById('gyro'),
        val: 0,
        offset: 0,
        visualVal: 0,
        arm: document.getElementById('gyro-arm'),
        number: document.getElementById('gyro-number')
    },
    robotDiagram: {
        arm: document.getElementById('robot-arm')
    },
    example: {
        button: document.getElementById('example-button'),
        readout: document.getElementById('example-readout').firstChild
    },
    autoSelect: document.getElementById('auto-select'),
    armPosition: document.getElementById('arm-position'),
    compress_command: document.getElementById('compress_command'),
    hatch_toggle: document.getElementById('hatch'),
    superstructure_toggle: document.getElementById('superstructure'),
    drop_toggle: document.getElementById('drop'),
    drop_arms_toggle: document.getElementById('arms'),
    deploy_toggle: document.getElementById('deploy'),
    //neutralize_button: documentment.getElementById('neutralize')
    hatch: [
        document.getElementById('hatch1'),
        document.getElementById('hatch2')
    ],
    test: document.getElementById('doesitwork')
};

let moveHatch = (xx) => {
    for (i = 0; i < ui.hatch.length; i++) {
        ui.hatch[i].setAttribute("x", xx);
    };
};

let cylinders = [
    "Hatch Mech",
    "Superstructure",
    "Hatch Deploy",
    "Drop Arms",
    "Floor Drop"
];

// Key Listeners

// Gyro rotation
let updateGyro = (key, value) => {
    ui.gyro.val = value;
    ui.gyro.visualVal = Math.floor(ui.gyro.val - ui.gyro.offset);
    ui.gyro.visualVal %= 360;
    if (ui.gyro.visualVal < 0) {
        ui.gyro.visualVal += 360;
    }
    ui.gyro.arm.style.transform = `rotate(${ui.gyro.visualVal}deg)`;
    ui.gyro.number.innerHTML = ui.gyro.visualVal + 'º';
};
NetworkTables.addKeyListener('/Robot/angle', updateGyro);

// The following case is an example, for a robot with an arm at the front.
NetworkTables.addKeyListener('/SmartDashboard/arm/encoder', (key, value) => {
    // Rotate the arm in diagram to match real arm
    if (value > 192) value = 192;
    moveHatch(value);
});

// This button is just an example of triggering an event on the robot by clicking a button.
NetworkTables.addKeyListener('/SmartDashboard/example_variable', (key, value) => {
    // Set class active if value is true and unset it if it is false
    ui.example.button.classList.toggle('active', value);
    ui.example.readout.data = 'Value is ' + value;
});

NetworkTables.addKeyListener('/Robot/time', (key, value) => {
    // We assume here that value is an integer representing the number of seconds left.
    // For M:SS ui.timer.innerHTML = value < 0 ? '0:00' : Math.floor(value / 60) + ':' + (value % 60 < 10 ? '0' : '') + value % 60;
    ui.timer.innerHTML = value < 0 ? '0' : value;
    if (value > 30) {
        ui.timer.style.backgroundColor = "#222";        // 135-31
    } else if (value <= 30 && value > 5) {
        ui.timer.style.backgroundColor = "orange";      // 30-6
    } else if (value <= 5) {
        ui.timer.style.backgroundColor = "red";         // 0-5
    };

});

// Load list of prewritten autonomous modes
NetworkTables.addKeyListener('/SmartDashboard/autonomous/modes', (key, value) => {
    // Clear previous list
    while (ui.autoSelect.firstChild) {
        ui.autoSelect.removeChild(ui.autoSelect.firstChild);
    }
    // Make an option for each autonomous mode and put it in the selector
    for (let i = 0; i < value.length; i++) {
        var option = document.createElement('option');
        option.appendChild(document.createTextNode(value[i]));
        ui.autoSelect.appendChild(option);
    }
    // Set value to the already-selected mode. If there is none, nothing will happen.
    ui.autoSelect.value = NetworkTables.getValue('/SmartDashboard/currentlySelectedMode');
});

// Load list of prewritten autonomous modes
NetworkTables.addKeyListener('/SmartDashboard/autonomous/selected', (key, value) => {
    ui.autoSelect.value = value;
});

ui.test.onclick = () => {
    alert(ui.hatch[0].getAttribute('x'));
}

ui.compress_command.onclick = () => {

    // Get current compressor status
    let compress_status = (NetworkTables.getValue('/SmartDashboard/Stop Compress/running') === false);

    // Switch the compress modes
    if (compress_status) {
        NetworkTables.putValue('/SmartDashboard/Stop Compress/running', true);
    } else if (!compress_status) {
        NetworkTables.putValue('/SmartDashboard/Stop Compress/running', false);
    }

    // Update the color of the button
    compress_status = (NetworkTables.getValue('/SmartDashboard/Stop Compress/running') === false);
    if (compress_status) {
        ui.compress_command.style.backgroundColor = 'green';
    } else {
        ui.compress_command.style.backgroundColor = 'red';
    }

};

ui.drop_arms_toggle.onclick = () => {

    // Get current compressor status
    let toggle_status = (ui.drop_arms_toggle.style.backgroundColor === 'green');

    // Switch the compress modes
    if (toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Drop Arms Retracted/running', true);
    } else if (!toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Drop Arms Extended/running', true);
    }

    // Update the color of the button
    toggle_status = (ui.drop_arms_toggle.style.backgroundColor === 'green');
    if (toggle_status) {
        ui.drop_arms_toggle.style.backgroundColor = 'red';
    } else {
        ui.drop_arms_toggle.style.backgroundColor = 'green';
    }

};

ui.deploy_toggle.onclick = () => {

    // Get current compressor status
    let toggle_status = (ui.deploy_toggle.style.backgroundColor === 'green');

    // Switch the compress modes
    if (toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Hatch Deploy Retracted/running', true);
    } else if (!toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Hatch Deploy Extended/running', true);
    }

    // Update the color of the button
    toggle_status = (ui.deploy_toggle.style.backgroundColor === 'green');
    if (toggle_status) {
        ui.deploy_toggle.style.backgroundColor = 'red';
    } else {
        ui.deploy_toggle.style.backgroundColor = 'green';
    }

};

ui.superstructure_toggle.onclick = () => {

    // Get current compressor status
    let toggle_status = (ui.superstructure_toggle.style.backgroundColor === 'green');

    // Switch the compress modes
    if (toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Superstructure Retracted/running', true);
    } else if (!toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Superstructure Extended/running', true);
    }

    // Update the color of the button
    toggle_status = (ui.superstructure_toggle.style.backgroundColor === 'green');
    if (toggle_status) {
        ui.superstructure_toggle.style.backgroundColor = 'red';
    } else {
        ui.superstructure_toggle.style.backgroundColor = 'green';
    }

};

ui.drop_toggle.onclick = () => {

    // Get current compressor status
    let toggle_status = (ui.drop_toggle.style.backgroundColor === 'green');

    // Switch the compress modes
    if (toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Floor Drop Retracted/running', true);
    } else if (!toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Floor Drop Extended/running', true);
    }

    // Update the color of the button
    toggle_status = (ui.drop_toggle.style.backgroundColor === 'green');
    if (toggle_status) {
        ui.drop_toggle.style.backgroundColor = 'red';
    } else {
        ui.drop_toggle.style.backgroundColor = 'green';
    }

};

ui.hatch_toggle.onclick = () => {

    // Get current compressor status
    let toggle_status = (ui.hatch_toggle.style.backgroundColor === 'green');

    // Switch the compress modes
    if (toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Hatch Mech Retracted/running', true);
    } else if (!toggle_status) {
        NetworkTables.putValue('/SmartDashboard/Hatch Mech Extended/running', true);
    }

    // Update the color of the button
    toggle_status = (ui.hatch_toggle.style.backgroundColor === 'green');
    if (toggle_status) {
        ui.hatch_toggle.style.backgroundColor = 'red';
    } else {
        ui.hatch_toggle.style.backgroundColor = 'green';
    }

};

/*ui.neutralize_button.onclick() = () => {
    for (i = 0; y < length(cylinders); i++) {
        NetworkTables.putValue('/SmartDashboard/' + cylinders[i] + ' Neutral/running', true);
    }
}*/

// The rest of the doc is listeners for UI elements being clicked on
ui.example.button.onclick = function() {
    // Set NetworkTables values to the opposite of whether button has active class.
    NetworkTables.putValue('/SmartDashboard/example_variable', this.className != 'active');
};
// Reset gyro value to 0 on click
ui.gyro.container.onclick = function() {
    // Store previous gyro val, will now be subtracted from val for callibration
    ui.gyro.offset = ui.gyro.val;
    // Trigger the gyro to recalculate value.
    updateGyro('/SmartDashboard/drive/navx/yaw', ui.gyro.val);
};
// Update NetworkTables when autonomous selector is changed
ui.autoSelect.onchange = function() {
    NetworkTables.putValue('/SmartDashboard/autonomous/selected', this.value);
};
// Get value of arm height slider when it's adjusted
ui.armPosition.oninput = function() {
    NetworkTables.putValue('/SmartDashboard/arm/encoder', parseInt(this.value));
};


addEventListener('error',(ev)=>{
    ipc.send('windowError',{mesg:ev.message,file:ev.filename,lineNumber:ev.lineno})
});