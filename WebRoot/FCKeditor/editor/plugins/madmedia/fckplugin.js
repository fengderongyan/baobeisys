/*
 * For FCKeditor 2.3
 * 
 * File Name: fckplugin.js
 * 	Add a toolbar button to insert media.
 * 
 * File Authors:
 * 		Madpolice (madpolice_dong@163.com) 20060726
 */


// Register the related commands.
FCKCommands.RegisterCommand( 'MadMedia'		, new FCKDialogCommand( FCKLang['DlgMadMediaTitle']	, FCKLang['DlgMadMediaTitle']		, FCKConfig.PluginsPath + 'madmedia/media.html'	, 450, 350 ) ) ;

// Create the "Media" toolbar button.
var oFindItem		= new FCKToolbarButton( 'MadMedia', FCKLang['MadMediaInsertMedia'] ) ;
oFindItem.IconPath	= FCKConfig.PluginsPath + 'madmedia/media.gif' ;

FCKToolbarItems.RegisterItem( 'MadMedia', oFindItem ) ;			// 'MadMedia' is the name used in the Toolbar config.
