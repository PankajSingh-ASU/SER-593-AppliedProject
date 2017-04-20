using System;
using SQLite;
namespace DynamicInputs
{
	public class SampleModel
	{
		string path;
		SQLiteConnection database;
		public SampleModel(string p)
		{
			path = p;
		}



		public void createConnection()
		{
			database = new SQLiteConnection(path);
		}
		public string createTable()
		{
			string message;
			try
			{
				
				database.CreateTable<MetaDataTable>();
				message = "Created successfully";
			}
			catch
			{
				message = "Error in table creation";
			}
			return message;
		}

	}
}
