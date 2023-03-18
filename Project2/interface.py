#The file interface.py contains the code for the GUI
#pip install PyQt5
from PyQt5 import QtWidgets
from PyQt5 import uic
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *    #from PyQt5.QtCore import Qt
import sys

#from explain import process, init_conn

class ScrollableLable(QScrollArea):

    def _init_(self, *args, **kwargs):
        QScrollArea._init_(self, *args, **kwargs)
        widget = QWidget(self)
        self.setWidgetResizable(True)
        self.setWidget(widget)
        layout = QVBoxLayout(widget)     #not necessary?
        self.label = QLabel(widget)
        self.label.setAlignment(Qt.AlignLeft | Qt.AlignTop)
        self.label.setFont(QFont('Arial', 15))
        self.label.setStyleSheet("background-color: beige; border: 1px solid black;")
        
        self.input_sql = self.findChild(QTextEdit, "input_query")
        self.label_qep = self.findChild(QLabel, "text_plan")
        self.btn_analyse = self.findChild(QPushButton, "btn_analyse")
        self.btn_clear = self.findChild(QPushButton, "btn_clear")
        self.list_database = self.findChild(QComboBox, "combo_databases")
        self.tree_attrs = self.findChild(QTreeWidget, "tree_attrs")
        
        layout.addWidget(self.label)

    def setText(self, text):
        self.label.setText(text)

class MyWindow(QMainWindow):
    def _init_(self):
        super(MyWindow, self)._init_()
        self.setGeometry(300, 50, 1600, 900)
        self.setWindowTitle("Application GUI")

        # Output text for query and annotation
        self.queryOutput = ScrollableLable(self)
        self.queryExplain = ScrollableLable(self)

        # Button for running algorithm
        self.submitButton = QtWidgets.QPushButton(self)

        # Textbox for query and db name
        self.queryTextbox = QTextEdit(self)
        self.dbNameTextbox = QTextEdit(self)

        # Label to indicate which db name is the app currently connected to
        self.dbNameLabel = ScrollableLable(self)

        # Error message box
        self.error_dialog = QtWidgets.QErrorMessage()

        self.initUI()

        # Db connection settings 
        self.connection = None
        self.dbName = ''

    def initUI(self):
        self.queryTextbox.move(30, 20)
        self.queryTextbox.resize(750, 350)
        self.queryTextbox.setFont(QFont('Arial', 15))
        self.queryTextbox.setPlaceholderText("Insert SQL Query Here :")

        self.dbNameTextbox.move(820, 140)
        self.dbNameTextbox.resize(300, 100)
        self.dbNameTextbox.setFont(QFont('Arial', 15))
        self.dbNameTextbox.setPlaceholderText("Insert Database Name Here :")

        self.queryOutput.setText("Output Query :")
        self.queryOutput.move(30, 400)
        self.queryOutput.resize(770, 450)
        self.queryOutput.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryOutput.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryExplain.setText("Explain for Output Query :")
        self.queryExplain.move(820, 400)
        self.queryExplain.resize(750, 450)
        self.queryExplain.setHorizontalScrollBarPolicy(Qt.ScrollBarAlwaysOn)
        self.queryExplain.setVerticalScrollBarPolicy(Qt.ScrollBarAlwaysOn)

        self.queryOutput.verticalScrollBar().valueChanged.connect(
        self.queryExplain.verticalScrollBar().setValue)
        self.queryExplain.verticalScrollBar().valueChanged.connect(
        self.queryOutput.verticalScrollBar().setValue)

        self.dbNameLabel.move(820, 20)
        self.dbNameLabel.resize(300, 100)
        self.dbNameLabel.setText(f"Current Database Name: ")

        self.submitButton.setText("Submit Query")
        self.submitButton.setFont(QFont('Arial', 15))
        self.submitButton.clicked.connect(self.onClick)
        self.submitButton.move(820, 270)
        self.submitButton.resize(300, 100)

    def setOnAnalyseClicked(self, callback):
        if callback:
            self.btn_analyse.clicked.connect(callback)    

    def onClick(self):
        if self.dbName != self.dbNameTextbox.toPlainText():
            try:
                self.connection = init_conn(self.dbNameTextbox.toPlainText())
                self.dbName = self.dbNameTextbox.toPlainText()
                self.dbNameLabel.setText(f"Current DB Name: {self.dbName}")
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")
        if self.connection is not None:
            try:
                query, annotation = process(self.connection, self.queryTextbox.toPlainText())
                self.queryOutput.setText('\n'.join(query))
                self.queryExplain.setText('\n'.join(annotation))
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")

    def showError(self, errMessage, execption=None):
        dialog = QMessageBox()
        dialog.setStyleSheet("QLabel{min-width: 300px;}");
        dialog.setWindowTitle("Error")
        #dialog.setIcon(QMessageBox.Warning)
        dialog.setText(errMessage)
        if execption is not None:
            dialog.setDetailedText(str(execption))
        dialog.setStandardButtons(QMessageBox.Ok)
        #dialog.buttonClicked.connect(cb)
        dialog.exec_()
        
def window():
    app = QApplication(sys.argv)
    win = MyWindow()

    win.show()
    sys.exit(app.exec_())


def main():
    window()


if __name__ == '__main__':
    main()