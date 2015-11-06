
Ext.onReady(function(){
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'season'},
		{name: 'round'},
		{name: 'start_time'},
		{name: 'end_time'},
		{name: 'match_count'},
		{name: 'log'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/daily_update_log_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
        							         
    var data_grid_panel = new Ext.grid.GridPanel({
        store: store,
        width: 600,
        height: 645,
        region:'center',
        loadMask: true,
        columns: [
           new Ext.grid.RowNumberer()
        ,{
            id: 'id',
            header: '编号',
            dataIndex: 'id',
            width: 55,
        },{
            id: 'season',
            header: '赛季',
            dataIndex: 'season',
            width: 50,
        },{
            id: 'round',
            header: '轮次',
            dataIndex: 'round',
            width: 50,
        },{
            id: 'log',
            header: '状态',
            dataIndex: 'log',
            width: 80,
        },{
            id: 'match_count',
            header: '比赛场数',
            dataIndex: 'match_count',
            align: 'right',
            width: 80,
        },{
            id: 'start_time',
            header: '开始时间',
            dataIndex: 'start_time',
            width: 150,
        },{
            id: 'end_time',
            header: '结束时间',
            dataIndex: 'end_time',
            width: 150,
        }],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var downurl_panel = new Ext.Panel({
        title: '批处理日志',
        layout: 'border',
        renderTo: 'data_div',
        layoutConfig: {
            columns: 1
        },
        height:650,
        items: [data_grid_panel]
    });
    
    store.load();
    
});