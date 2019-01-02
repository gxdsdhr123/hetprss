(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['exports', 'echarts'], factory);
    } else if (typeof exports === 'object' && typeof exports.nodeName !== 'string') {
        // CommonJS
        factory(exports, require('echarts'));
    } else {
        // Browser globals
        factory({}, root.echarts);
    }
}(this, function (exports, echarts) {
    var log = function (msg) {
        if (typeof console !== 'undefined') {
            console && console.error && console.error(msg);
        }
    };
    if (!echarts) {
        log('ECharts is not Loaded');
        return;
    }
    var contrastColor = '#eee';
    var axisCommon = function () {
        return {
            axisLine: {
                lineStyle: {
                    color: contrastColor,
                    opacity:0.6
                }
            },
            axisTick: {
                lineStyle: {
                    color: contrastColor
                }
            },
            axisLabel: {
                textStyle: {
                    color: contrastColor
                }
            },
            splitLine: {
            	show:false,
                lineStyle: {
                    type: 'dashed',
                    color: '#aaa'
                }
            },
            splitArea: {
                areaStyle: {
                    color: contrastColor
                }
            }
        };
    };

    //var colorPalette = ['#dd6b66','#759aa0','#e69d87','#8dc1a9','#ea7e53','#eedd78','#73a373','#73b9bc','#7289ab', '#91ca8c','#f49f42'];
    var colorPalette = ['#5C9294','#DEB241','#8CC5B1','#B1F1C8',
    					'#B0C882','#BFA0ED','#F1DDB1','#74B3E3',
    					'#85ABD6','#B0E28B','#54D9B8','#718CE6'
    					];
    var theme = {
        color: colorPalette,
        //backgroundColor: '#333',
        tooltip: {
            axisPointer: {
                lineStyle: {
                    color: contrastColor
                },
                crossStyle: {
                    color: contrastColor
                }
            }
        },
        legend: {
            textStyle: {
                color: contrastColor
            }
        },
        textStyle: {
            color: contrastColor
        },
        title: {
            textStyle: {
                color: contrastColor
            }
        },
        toolbox: {
            iconStyle: {
                normal: {
                    borderColor: contrastColor
                }
            }
        },
        dataZoom: {
            textStyle: {
                color: contrastColor
            }
        },
        visualMap: {
            textStyle: {
                color: contrastColor
            }
        },
        timeline: {
            lineStyle: {
                color: contrastColor
            },
            itemStyle: {
                normal: {
                    color: colorPalette[1]
                }
            },
            label: {
                normal: {
                    textStyle: {
                        color: contrastColor
                    }
                }
            },
            controlStyle: {
                normal: {
                    color: contrastColor,
                    borderColor: contrastColor
                }
            }
        },
        timeAxis: axisCommon(),
        logAxis: axisCommon(),
        valueAxis: axisCommon(),
        categoryAxis: axisCommon(),

        line: {
            symbol: 'circle',
            symbolSize:8
        },
        graph: {
            color: colorPalette
        },
        gauge: {
            title: {
                textStyle: {
                    color: contrastColor
                }
            }
        },
        candlestick: {
            itemStyle: {
                normal: {
                    color: '#FD1050',
                    color0: '#0CF49B',
                    borderColor: '#FD1050',
                    borderColor0: '#0CF49B'
                }
            }
        }
    };
    theme.categoryAxis.splitLine.show = false;
    echarts.registerTheme('dark', theme);
}));