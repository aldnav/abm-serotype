// split data to datapoints

$(document).ready(function(){
	var list = $('#params-list');
	    list.append('<li>'+config.population+' Population</li>');
	    list.append('<li>'+config.duration+' '+config.timeUnit+'</li>');
	    list.append('<li>'+config.infectionProbability+' Infection Probability</li>');
	    list.append('<li>'+config.ser1InitInfected+' S1 Init Infected</li>');
	    list.append('<li>'+config.ser2InitInfected+' S2 Init Infected</li>');
	    list.append('<li>'+config.ser1InitResistant+' S1 Init Resistant</li>');
	    list.append('<li>'+config.ser2InitResistant+' S2 Init Resistant</li>');

	// alert($('input[name=show]:checked', '#options-form').val());
	$('input[type=radio][name=show]', '#options-form').change(function() {
        if (this.value == 'all') {
            $("#ser1").dxChart({
                panes: [
                    { name: 'topPane' },
                    { name: 'bottomPane' }
                ],
            	series: [{
            		name: 'Susceptible I',
            		valueField: 'susceptible1',
                    pane: 'topPane',
                    color: '#2ECC71'
            	},{
            		name: 'Infected I',
            		valueField: 'infected1',
                    pane: 'topPane',
                    color: '#E67E22'
            	}, {
            		name: 'Resistant I',
            		valueField: 'resistant1',
                    pane: 'topPane',
                    color: '#3498DB'
            	}, {
                    name: 'Susceptible II',
                    valueField: 'susceptible2',
                    pane: 'bottomPane',
                    color: '#1ABC9C'
                }, {
                    name: 'Infected II',
                    valueField: 'infected2',
                    pane: 'bottomPane',
                    color: '#E74C3C'
                }, {
                    name: 'Resistant II',
                    valueField: 'resistant2',
                    pane: 'bottomPane',
                    color: '#34495E'
                },]
            });
        } else if (this.value == 'susceptible') {
            $("#ser1").dxChart({
                panes: [
                    { name: 'topPane' },
                    { name: 'bottomPane' }
                ],
            	series: [{
            		name: 'Susceptible I',
            		valueField: 'susceptible1',
                    pane: 'topPane',
                    color: '#2ECC71'
            	}, {
                    name: 'Susceptible II',
                    valueField: 'susceptible2',
                    pane: 'bottomPane',
                    color: '#1ABC9C'
                },]
            });
        } else if (this.value == 'infected') {
        	$("#ser1").dxChart({
                panes: [
                    { name: 'topPane' },
                    { name: 'bottomPane' }
                ],
            	series: [{
            		name: 'Infected I',
            		valueField: 'infected1',
                    pane: 'topPane',
                    color: '#E67E22'
            	}, {
                    name: 'Infected II',
                    valueField: 'infected2',
                    pane: 'bottomPane',
                    color: '#E74C3C'
                },]
            });
        } else if (this.value == 'resistant') {
        	$("#ser1").dxChart({
                panes: [
                    { name: 'topPane' },
                    { name: 'bottomPane' }
                ],
            	series: [ {
            		name: 'Resistant I',
            		valueField: 'resistant1',
                    pane: 'topPane',
                    color: '#3498DB'
            	}, {
                    name: 'Resistant II',
                    valueField: 'resistant2',
                    pane: 'bottomPane',
                    color: '#34495E'
                },]
            });
        } else if (this.value == 'all-in-one') {
            $("#ser1").dxChart({
                panes: [
                    { name: 'topPane' }
                ],
                series: [{
                    name: 'Susceptible I',
                    valueField: 'susceptible1',
                    pane: 'topPane',
                    color: '#2ECC71'
                },{
                    name: 'Infected I',
                    valueField: 'infected1',
                    pane: 'topPane',
                    color: '#E67E22'
                }, {
                    name: 'Resistant I',
                    valueField: 'resistant1',
                    pane: 'topPane',
                    color: '#3498DB'
                }, {
                    name: 'Susceptible II',
                    valueField: 'susceptible2',
                    pane: 'topPane',
                    color: '#1ABC9C'
                }, {
                    name: 'Infected II',
                    valueField: 'infected2',
                    pane: 'topPane',
                    color: '#E74C3C'
                }, {
                    name: 'Resistant II',
                    valueField: 'resistant2',
                    pane: 'topPane',
                    color: '#34495E'
                },]
            });
        }
    });
});