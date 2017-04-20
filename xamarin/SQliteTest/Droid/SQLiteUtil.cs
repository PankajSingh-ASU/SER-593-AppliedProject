using System;
using Android.Database.Sqlite;
using Android.Content;
using System.IO;
using System.Reflection;
using Android;

namespace SQliteTest
{
	public class SQLiteUtil : SQLiteOpenHelper
	{
		private static string DATABASE_DIRECTORY = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
		private static string DATABASE_FILE_NAME = "Spotlight.sqlite";
		private static int VERSION = 1;
		private Context context = null;

		public SQLiteUtil(Context _context) : base(_context, DATABASE_FILE_NAME, null, VERSION)
		{
			this.context = _context;
		}

		#region implemented abstract members of SQLiteOpenHelper

		public override void OnCreate(SQLiteDatabase _database)
		{

		}

		public override void OnUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{

		}

		#endregion

		public override SQLiteDatabase WritableDatabase
		{
			get
			{
				return CreateSQLiteDatabase();
			}
		}

		public SQLiteDatabase CreateSQLiteDatabase()
		{
			SQLiteDatabase _objSQLiteDatabase = null;
			string _strSQLitePathOnDevice = GetSQLitePathOnDevice();
			Stream _streamSQLite = null;
			Stream _streamSQLite1 = null;
			Stream _streamSQLite2 = null;
			FileStream _streamWrite = null;
			Boolean isSQLiteInitialized = false;
			Console.WriteLine("SQLitePathOnDevice :" + _strSQLitePathOnDevice);
			try
			{
				if (File.Exists(_strSQLitePathOnDevice))
				{
					System.IO.File.Delete(_strSQLitePathOnDevice);
					isSQLiteInitialized = true;
				}
				//else
				//{
					Console.WriteLine("Else part ");
			//	_streamSQLite = Assembly.GetExecutingAssembly().GetManifestResourceStream("SQliteTest.test.db");
					_streamSQLite = context.Resources.Assets.Open("SQliteTest.test.db");
			//_streamSQLite2 = context.Resources.OpenRawResource(2130968576);
				_streamWrite = new FileStream(_strSQLitePathOnDevice, FileMode.OpenOrCreate, FileAccess.Write);
					if (_streamSQLite != null && _streamWrite != null)
					{
						if (CopySQLiteOnDevice(_streamSQLite, _streamWrite))
						{
							isSQLiteInitialized = true;
						}
					}
				//}
				if (isSQLiteInitialized)
				{
					_objSQLiteDatabase = SQLiteDatabase.OpenDatabase(_strSQLitePathOnDevice, null, DatabaseOpenFlags.OpenReadonly);
				}
			}
			catch (Exception _exception)
			{
				MethodBase _currentMethod = MethodInfo.GetCurrentMethod();
				Console.WriteLine(String.Format("CLASS : {0}; METHOD : {1}; EXCEPTION : {2}"
					, _currentMethod.DeclaringType.FullName
					, _currentMethod.Name
					, _exception.Message));
			}
			return _objSQLiteDatabase;
		}

		private string GetSQLitePathOnDevice()
		{
			string _strSQLitePathOnDevice = string.Empty;
			try
			{
				_strSQLitePathOnDevice = Path.Combine(DATABASE_DIRECTORY, DATABASE_FILE_NAME);
			}
			catch (Exception _exception)
			{
				MethodBase _currentMethod = MethodInfo.GetCurrentMethod();
				Console.WriteLine(String.Format("CLASS : {0}; METHOD : {1}; EXCEPTION : {2}"
					, _currentMethod.DeclaringType.FullName
					, _currentMethod.Name
					, _exception.Message));
			}
			return _strSQLitePathOnDevice;
		}

		private bool CopySQLiteOnDevice(Stream _streamSQLite, Stream _streamWrite)
		{
			bool _isSuccess = false;
			int _length = 256;
			Byte[] _buffer = new Byte[_length];
			try
			{
				int _bytesRead = _streamSQLite.Read(_buffer, 0, _length);
				while (_bytesRead > 0)
				{
					_streamWrite.Write(_buffer, 0, _bytesRead);
					_bytesRead = _streamSQLite.Read(_buffer, 0, _length);
				}
				_isSuccess = true;
			}
			catch (Exception _exception)
			{
				MethodBase _currentMethod = MethodInfo.GetCurrentMethod();
				Console.WriteLine(String.Format("CLASS : {0}; METHOD : {1}; EXCEPTION : {2}"
					, _currentMethod.DeclaringType.FullName
					, _currentMethod.Name
					, _exception.Message));
			}
			finally
			{
				_streamSQLite.Close();
				_streamWrite.Close();
			}
			return _isSuccess;
		}
	}
}
