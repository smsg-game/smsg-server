
Ext.onReady(function(){

    var degree_store = new Ext.data.SimpleStore({
                       fields: ['degree', 'degree_name'],
                       data: [
                          ['1', '一级'],
                          ['2', '二级'],
                          ['3', '三级'],
                          ['4', '四级'],
                          ['5', '五级'],
                          ['6', '六级'],
                          ['7', '七级'],
                          ['8', '八级'],
                          ['9', '九级'],
                          ['10', '十级'],
                          ['11', '十一级'],
                       ]
                             
    });
    
    var degree_combobox = new Ext.form.ComboBox({
                 id: 'degree_combobox',
                 store: degree_store,
                 mode: 'local',
                 displayField:'degree_name',
                 valueField:'degree',
                 triggerAction: 'all',
                 forceSelection: true,
                 width:80,
                 value: '1',
                 listeners: {
                   "select": function(){
                      var degree = this.getValue();
                      store.load({
                         params: {
                            start: 0,
                            limit:20,
                            degree: degree,
                         }
                      });
                   }
                 }
     });
    
	var data_record = new Ext.data.Record.create([
		{name: 'id'},
		{name: 'degree'},
		{name: 'no'},
		{name: 'team_count'},
		{name: 'status'},
		{name: 'update_finish'},
		{name: 'updated_time'},
	]);
 	
	var data_read = new Ext.data.JsonReader({totalProperty: 'total', root: 'infos', id: 'id'}, data_record);
	
	var store = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: '/admin/league_list_json/'}),
        						    reader: data_read,
        							remoteSort: true ,
        					       });
        					       
    store.on('beforeload', function() {  
       var degree = degree_combobox.getValue() ;
       this.baseParams = {  
         'degree': degree,
       };  
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
            align: 'right',
            width: 55,
        },{
            id: 'degree',
            header: '等级',
            align: 'right',
            dataIndex: 'degree',
            width: 50,
        },{
            id: 'no',
            header: '序号',
            align: 'right',
            dataIndex: 'no',
            width: 50,
        },{
            id: 'team_count',
            header: '球队数',
            dataIndex: 'team_count',
            width: 80,
            align: 'right',
            sortable: true,
        },{
            id: 'status',
            header: '状态',
            dataIndex: 'status',
            align: 'right',
            width: 80,
            align: 'center',
        },{
            id: 'update_finish',
            header: '更新完成',
            dataIndex: 'update_finish',
            width: 150,
            align: 'center',
        },{
            id: 'updated_time',
            header: '更新时间',
            dataIndex: 'updated_time',
            width: 150,
        }],
        tbar: [
            degree_combobox,
        ],
        bbar: new Ext.PagingToolbar({
	        pageSize: 20,
	        store: store,
	        displayInfo: true,
	        displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
	        emptyMsg: "没有记录"
	    })
      
    });
    var downurl_panel = new Ext.Panel({
        title: '联赛管理',
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