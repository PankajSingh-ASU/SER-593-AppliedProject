
using System;
using Android.Content;
using Android.Widget;
using Android.App;
namespace SQliteTest.Droid
{
	public class DynamicDatePicker
	{
		EditText editText;
		public DynamicDatePicker(EditText ed)
		{
			editText = ed;
		}

			public void createDatePicker(Context context)
		{

			// reference http://stackoverflow.com/questions/37477860/datepicker-how-to-popup-datepicker-when-click-on-edittext-using-c-sharp-xamarin

			editText.Click += (sender, e) =>
			{
				DateTime today = DateTime.Today;
				DatePickerDialog dialog = new DatePickerDialog(context, OnDateSet, today.Year, today.Month - 1, today.Day);
				dialog.DatePicker.MinDate = today.Millisecond;
				dialog.Show();
			};


		}
		void OnDateSet(object sender, DatePickerDialog.DateSetEventArgs e)
		{
			editText.Text = e.Date.ToShortDateString();
		}


	}
}
