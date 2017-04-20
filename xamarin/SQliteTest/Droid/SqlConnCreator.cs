using System;

using System.IO;
using SQLite;
namespace SQliteTest.Droid
{
	public class SqlConnCreator
	{
		public SqlConnCreator()
		{
		}
		public static string  getConnectionPath()
		{
			string dbPath = Path.Combine(
				Environment.GetFolderPath(Environment.SpecialFolder.Personal), "Spotlight.sqlite");
			Console.WriteLine(dbPath);




			return dbPath;
		}
	}
}
