
Ext.onReady(function(){
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'client_name'},
		{name: 'ip'},
		{name: 'status'},
		{name: 'log'},
		{name: 'log_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/betch_log_json/'}),
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
            id: 'client_name',
            header: '客户端',
            dataIndex: 'client_name',
            width: 150,
        },{
            id: 'ip',
            header: '客户端IP',
            dataIndex: 'ip',
            width: 80,
        },{
            id: 'status',
            header: '状态',
            dataIndex: 'status',
            width: 50,
        },{
            id: 'log',
            header: '内容',
            dataIndex: 'log',
            width: 650,
            renderer: function(value){
              return "<span title=\"" + value + "\">" + value + "</span>" ;
            }
        },{
            id: 'log_time',
            header: '日志时间',
            dataIndex: 'log_time',
            width: 200,
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