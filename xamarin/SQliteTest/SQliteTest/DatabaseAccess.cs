using System;
using System.Collections.Generic;
using SQLite.Net;
using SQLite.Net.Async;
using SQLite.Net.Interop;
using System.IO;

namespace SQliteTest
{
	public class DatabaseAccess
	{
		//string dbPath;
		SQLiteAsyncConnection db;
		public DatabaseAccess(string path, ISQLitePlatform sqlitePlatform)
		{
			//dbPath = path;
			var connectionFactory = new Func<SQLiteConnectionWithLock>(() => new SQLiteConnectionWithLock(sqlitePlatform, new SQLiteConnectionString(path, storeDateTimeAsTicks: false)));

			db = new SQLiteAsyncConnection(connectionFactory);
		}
		public List<TableInfo> schemaQuery(string table)
		{
			var output = "";
			output += "\nSchema query example: ";


			var query = "pragma table_info('"+table+"')";

			var existingCols = db.QueryAsync<TableInfo>(query).Result;

			//foreach (var s in existingCols)
			//{
			//	output += "\n" + s.name + " " + s.type;
			//}
			////Console.WriteLine(output);
			////output += query;

			return existingCols;
		}

		public List<String> tableQueries()
		{
			var output = "";
			output += "Table query example: ";
			List<string> st = new List<string>();

			var query = "SELECT name FROM sqlite_master WHERE type=\"table\"";
			var t = db.QueryAsync<MasterTableInfo>(query);



			var tables = t.Result;
			foreach (var s in tables)
			{
				st.Add(s.name);
			}


			return st;
		}

		public void saveTableValues(string table, List<TableInfo> columns, Dictionary<int, object> dict)
		{
			var query = "insert into " + table + "( ";
			var values = " Values( ";
			foreach(var c in columns)
			{
				//insert into myTable(name, mydate, value, dead) Values('abc', '5/11/2017', '24', '2');
				var val = dict[c.cid];
				if (val != null)
				{
					if ("text".Equals(c.type.ToLower())
					{
						val = "'" + val + "'";
					}
						
					query += c.name + " ,";
					values += val + " ,";
				}
			}
			query=query.Remove(query.Length - 1);
			values = values.Remove(values.Length - 1);
			query += ")";
			values += ")";

			query += values;
			var result=db.ExecuteAsync(query);

					    db.GetMappingAsync
			int rest = result.Result;

		}
	}

}
