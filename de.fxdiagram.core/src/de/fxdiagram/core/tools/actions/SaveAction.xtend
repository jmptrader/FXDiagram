package de.fxdiagram.core.tools.actions

import de.fxdiagram.core.XRoot
import de.fxdiagram.core.model.ModelSave
import eu.hansolo.enzo.radialmenu.SymbolType
import java.io.FileWriter
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.FileChooser
import java.io.File

class SaveAction implements DiagramAction {

	override matches(KeyEvent it) {
		isShortcutDown && code == KeyCode.S
	}
	
	override getSymbol() {
		SymbolType.SAVE
	}
	
	override getTooltip() {
		'Save diagram'
	}

	override perform(XRoot root) {
		if(root.diagram != null) {
			val file = if(root.fileName != null) {
				new File(root.fileName)
			} else {
				val fileChooser = new FileChooser()
				fileChooser.extensionFilters += new FileChooser.ExtensionFilter("FXDiagram", "*.fxd")
				fileChooser.showSaveDialog(root.scene.window)
			} 
			if(file != null) {
				new ModelSave().save(root, new FileWriter(file))
				root.fileName = file.path
				root.needsSave = false
			}
		}
	}
}