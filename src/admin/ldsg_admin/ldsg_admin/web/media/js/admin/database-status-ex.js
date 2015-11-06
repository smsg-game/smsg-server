
Ext.onReady(function(){

	var data_record = new Ext.data.Record.create([
		{name: 'Id'},
		{name: 'User'},
		{name: 'Host'},
		{name: 'db'},
		{name: 'Command'},
		{name: 'Time'},
		{name: 'State'},
		{name: 'Info'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'Id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/database_status_json/'}),
        						    reader: data_read,
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
            dataIndex: 'Id',
            align: 'right',
            width: 55,
        },{
            id: 'User',
            header: '用户',
            align: 'right',
            dataIndex: 'User',
            width: 50,
        },{
            id: 'Host',
            header: '客户端IP',
            dataIndex: 'Host',
            width: 200,
        },{
            id: 'db',
            header: '数据库',
            dataIndex: 'db',
            width: 80,
            align: 'right',
            sortable: true,
        },{
            id: 'Command',
            header: '命令',
            dataIndex: 'Command',
            align: 'right',
            width: 80,
            align: 'center',
        },{
            id: 'Time',
            header: '耗时(秒)',
            dataIndex: 'Time',
            width: 80,
            align: 'right',
        },{
            id: 'State',
            header: '状态',
            dataIndex: 'State',
            width: 150,
        },{
            id: 'Info',
            header: '详细信息',
            dataIndex: 'Info',
            width: 450,
        }] 
    });
    var downurl_panel = new Ext.Panel({
        title: '数据库状态',
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