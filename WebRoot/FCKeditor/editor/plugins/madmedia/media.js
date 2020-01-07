/*
 * For FCKeditor 2.3
 * 
 * File Name: media.js
 * 	Scripts related to the Media dialog window (see media.html).
 * 
 * File Authors:
 * 		Madpolice (madpolice_dong@163.com) 20060726
 */

var oEditor		= window.parent.InnerDialogLoaded() ;
var FCK			= oEditor.FCK ;
var FCKLang		= oEditor.FCKLang ;
var FCKConfig	= oEditor.FCKConfig ;

//#### Dialog Tabs

// Set the dialog tabs.
window.parent.AddTab( 'Info', oEditor.FCKLang.DlgInfoTab ) ;

if ( FCKConfig.FlashUpload )
	window.parent.AddTab( 'Upload', FCKLang.DlgLnkUpload ) ;


// Function called when a dialog tag is selected.
function OnDialogTabChange( tabCode )
{
	ShowE('divInfo'		, ( tabCode == 'Info' ) ) ;
	ShowE('divUpload'	, ( tabCode == 'Upload' ) ) ;
}

// Get the selected media embed (if available).
var oEmbed = FCK.Selection.GetSelectedElement() ;

window.onload = function()
{
	// Translate the dialog box texts.
	oEditor.FCKLanguageManager.TranslatePage(document) ;

	// Load the selected element information (if any).
	LoadSelection() ;

	// Show/Hide the "Browse Server" button.
	GetE('tdBrowse').style.display = FCKConfig.FlashBrowser	? '' : 'none' ;

	// Set the actual uploader URL.
	if ( FCKConfig.FlashUpload )
		GetE('frmUpload').action = FCKConfig.FlashUploadURL ;

	window.parent.SetAutoSize( true ) ;

	// Activate the "OK" button.
	window.parent.SetOkButton( true ) ;
}

function LoadSelection()
{
	if ( ! oEmbed ) {
		GetE('txtAttId').value = parseInt(Math.random()*7999)
		return ;
	} else {
		var sUrl = GetAttribute( oEmbed, 'src', '' ) ;
	
		GetE('txtUrl').value    = GetAttribute( oEmbed, 'src', '' ) ;
		GetE('txtWidth').value  = GetAttribute( oEmbed, 'width', '' ) ;
		GetE('txtHeight').value = GetAttribute( oEmbed, 'height', '' ) ;

	// Get Advances Attributes
		GetE('txtAttId').value		= oEmbed.id ;
		GetE('txtAlt').value = GetAttribute( oEmbed, 'title', '') ;
	
		GetE('chkAutoPlay').checked	= GetAttribute( oEmbed, 'autostart', 'true' ) == 'true' ;
	}
}

//#### The OK button was hit.
function Ok()
{
	if ( GetE('txtUrl').value.length == 0 )
	{
		window.parent.SetSelectedTab( 'Info' ) ;
		GetE('txtUrl').focus() ;

		alert( oEditor.FCKLang.DlgAlertUrl ) ;

		return false ;
	}

	if ( ! oEmbed ) {
		oEmbed		= FCK.CreateElement( 'EMBED' ) ;
	} else {
		oEditor.FCKUndo.SaveUndoStep() ;
	}
	UpdateEmbed( oEmbed ) ;

	return true ;
}

function UpdateEmbed( e )
{
	e.src = GetE('txtUrl').value ;
	SetAttribute( e, "width" , GetE('txtWidth').value ) ;
	SetAttribute( e, "height", GetE('txtHeight').value ) ;
	
	// Advances Attributes

	SetAttribute( e, 'id'	, GetE('txtAttId').value ) ;
	SetAttribute( e, 'title', GetE('txtAlt').value);
	
	SetAttribute( e, 'autostart', GetE('chkAutoPlay').checked ? 'true' : 'false' ) ;

}

function BrowseServer()
{
	OpenFileBrowser( FCKConfig.FlashBrowserURL, FCKConfig.FlashBrowserWindowWidth, FCKConfig.FlashBrowserWindowHeight ) ;
}

function SetUrl( url )
{
	GetE('txtUrl').value = url ;

	window.parent.SetSelectedTab( 'Info' ) ;
}

function OnUploadCompleted( errorNumber, fileUrl, fileName, customMsg )
{
	switch ( errorNumber )
	{
		case 0 :	// No errors
			alert( '上传成功' ) ;
			break ;
		case 1 :	// Custom error
			alert( customMsg ) ;
			return ;
		case 101 :	// Custom warning
			alert( customMsg ) ;
			break ;
		case 201 :
			alert( '服务器上存在一个同名文件. 上传的文件被自动更名为 "' + fileName + '"' ) ;
			break ;
		case 202 :
			alert( '文件类型非法' ) ;
			return ;
		case 203 :
			alert( "权限不足，无法上传。请检查您的服务器设置" ) ;
			return ;
		default :
			alert( '上传错误，错误号为: ' + errorNumber ) ;
			return ;
	}

	SetUrl( fileUrl ) ;
	GetE('frmUpload').reset() ;
}

var oUploadAllowedExtRegex	= new RegExp( FCKConfig.FlashUploadAllowedExtensions, 'i' ) ;
var oUploadDeniedExtRegex	= new RegExp( FCKConfig.FlashUploadDeniedExtensions, 'i' ) ;

function CheckUpload()
{
	var sFile = GetE('txtUploadFile').value ;
	
	if ( sFile.length == 0 )
	{
		alert( '上传文件不能为空' ) ;
		return false ;
	}
	
	if ( ( FCKConfig.FlashUploadAllowedExtensions.length > 0 && !oUploadAllowedExtRegex.test( sFile ) ) ||
		( FCKConfig.FlashUploadDeniedExtensions.length > 0 && oUploadDeniedExtRegex.test( sFile ) ) )
	{
		OnUploadCompleted( 202 ) ;
		return false ;
	}
	
	return true ;
}