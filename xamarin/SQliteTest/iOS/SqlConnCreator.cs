using System;

using System.IO;
using SQLite;
namespace SQliteTest.iOS
{
	public class SqlConnCreator
	{
		public SqlConnCreator()
		{
		}
		public static string  getConnectionPath()
		{
			string documentsPath = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
			 
			string dbPath = Path.Combine(documentsPath, "..", "Library", "Spotlight.sqlite");
			Console.WriteLine(dbPath);




			return dbPath;
		}
	}
}
