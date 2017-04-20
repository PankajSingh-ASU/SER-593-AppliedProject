using SQLite;
namespace DynamicInputs
{
	class MetaDataTable
	{
		[PrimaryKey, AutoIncrement, Column("_id")]
		public int Id { get; set; }

		public string tableName { get; set; }
		public string columnName { get; set; }
		public string columnType { get; set; }
	}
}