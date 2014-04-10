$(document).ready(
        $(function () {
            $("#ser1").dxChart({
            	title: {
            		text: 'Serotype Specific Dengue ABM'
            	},
            	series: {
				    type: 'line'
				},panes: [
                    { name: 'topPane' },
                    { name: 'bottomPane' }
                ],
            	dataSource: results,
            	commonSeriesSettings: {
            		argumentField: 'timeStep',
            	},
            	label: {
		            visible: true,
		            connector: {
		                visible: true
		            }
		        },
		        tooltip: {
			        enabled: true,
			    },
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
        })
     );