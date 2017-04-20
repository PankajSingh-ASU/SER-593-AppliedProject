
using Android.App;
using Android.Widget;
using Android.OS;
using SQLite.Net.Platform.XamarinAndroid;
using Android.Database.Sqlite;
using System;
using System.Collections.Generic;
using Android.Content;

namespace SQliteTest.Droid
{
	[Activity(Label = "SQliteTest", MainLauncher = true, Icon = "@mipmap/icon")]
	public class MainActivity : Activity
	{
		DatabaseAccess d;
		DynamicUICreator creator= new DynamicUICreator();

		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);

			// Set our view from the "main" layout resource
			SetContentView(Resource.Layout.Main);
			SQLiteUtil sq = new SQLiteUtil(this);
			SQLiteDatabase _objSQLiteDatabase = sq.WritableDatabase;
			// Get our button from the layout resource,
			// and attach an event to it
			var dbPath=SqlConnCreator.getConnectionPath();
			 d = new DatabaseAccess(dbPath,new SQLitePlatformAndroid() );



			//button.Click += delegate { 
				var data=d.tableQueries();
				Spinner spinner = FindViewById<Spinner>(Resource.Id.spinner1);
				spinner.ItemSelected += new EventHandler<AdapterView.ItemSelectedEventArgs>(spinner_ItemSelected);
			ArrayAdapter adapter = 
				new ArrayAdapter<string>(this, Android.Resource.Layout.SimpleSpinnerItem, data);


				spinner.Adapter = adapter;


			//};

		}
		private void spinner_ItemSelected(object sender, AdapterView.ItemSelectedEventArgs e)
		{
			Spinner spinner = (Spinner)sender;
			//Merchant merch = (Merchant)spinner.SelectedItem;
			string table = (string)spinner.GetItemAtPosition(e.Position);
			var columns=d.schemaQuery(table);

			var main = FindViewById<LinearLayout>(Resource.Id.mainLayout);

			var layout = main.FindViewById<ScrollView>(100);
			if (layout != null)
				main.RemoveView(layout);
			 layout=creator.createDynamicUI(this,columns);
			main.AddView(layout);

			//adding onclicklistener to Save button
			Button button = main.FindViewById<Button>(1001);
			button.Click += delegate
			{
				saveUIValues(table,columns);
			};


		}

		void saveUIValues(string table,List<TableInfo> columns)
		{
			var uiList = creator.uiList;
			InputStore ip = new InputStore();
			var dict = ip.getInputValues(uiList, columns);
			d.saveTableValues(table,columns,dict);
		}

		
}
}

