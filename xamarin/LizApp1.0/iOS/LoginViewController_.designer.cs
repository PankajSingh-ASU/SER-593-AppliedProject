// WARNING
//
// This file has been generated automatically by Xamarin Studio from the outlets and
// actions declared in your storyboard file.
// Manual changes to this file will not be maintained.
//
using Foundation;
using System;
using System.CodeDom.Compiler;
using UIKit;

namespace LizApp.iOS
{
    [Register ("LoginViewController")]
    partial class LoginViewController
    {
        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UITextField handler { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIPickerView picker { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UITextField recorder { get; set; }

        [Outlet]
        [GeneratedCode ("iOS Designer", "1.0")]
        UIKit.UIPickerView site { get; set; }

        void ReleaseDesignerOutlets ()
        {
            if (handler != null) {
                handler.Dispose ();
                handler = null;
            }

            if (picker != null) {
                picker.Dispose ();
                picker = null;
            }

            if (recorder != null) {
                recorder.Dispose ();
                recorder = null;
            }

            if (site != null) {
                site.Dispose ();
                site = null;
            }
        }
    }
}