from PyQt5 import QtWidgets
from PyQt5.QtWidgets import *
from PyQt5.QtGui import *
from PyQt5.QtCore import *
import sys
from project import *

#from annotation import process, init_conn


class ScrollableLabel(QScrollArea):

    def __init__(self, *args, **kwargs):
        QScrollArea.__init__(self, *args, **kwargs)
        widget = QWidget(self)
        self.setWidgetResizable(True)
        self.setWidget(widget)
        layout = QVBoxLayout(widget)
        self.label = QLabel(widget)
        self.label.setAlignment(Qt.AlignLeft | Qt.AlignTop)
        self.label.setFont(QFont('Arial', 15))
        self.label.setStyleSheet("background-color: beige; border: 1px solid black;")
        #self.input_sql = self.findChild(QTextEdit, "input_query")
        #self.label_qep = self.findChild(QLabel, "text_plan")
        #self.btn_analyse = self.findChild(QPushButton, "btn_analyse")
        #self.btn_clear = self.findChild(QPushButton, "btn_clear")
        #self.list_database = self.findChild(QComboBox, "combo_databases")
        #self.tree_attrs = self.findChild(QTreeWidget, "tree_attrs")
        layout.addWidget(self.label)

    def setText(self, text):
        self.label.setText(text)


class MyWindow(QMainWindow):
    def __init__(self):
        super(MyWindow, self).__init__()
        self.setGeometry(300, 50, 1600, 900)  # xpos, ypos, width, height
        self.setWindowTitle("Application GUI")

        # Output text for query and annotation
        self.queryOutput = ScrollableLabel(self)
        self.queryExplain = ScrollableLabel(self)

        # Button for running algorithm
        self.submitButton = QtWidgets.QPushButton(self)

        # Textbox for query and db name
        self.queryTextbox = QTextEdit(self)
        self.dbNameTextbox = QTextEdit(self)

        # Label to indicate which db name is the app currently connected to
        self.dbNameLabel = ScrollableLabel(self)

        # Error message box
        self.error_dialog = QtWidgets.QErrorMessage()

        self.UI()  # Call initUI

        # Db connection settings
        self.database = None
        self.dbName = ''

    def UI(self):
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

    def onClick(self):
        if self.dbName != self.dbNameTextbox.toPlainText():
            try:
                # A connection in need of explain.py's obtain message function
                self.database=Database(self.dbNameTextbox.toPlainText())
                self.dbName = self.dbNameTextbox.toPlainText()
                self.dbNameLabel.setText(f"Current DB Name: {self.dbName}")
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")
        if self.database is not None:
            try:
                # A connection in need of explain.py's output result function
                #def explain(self, query, query2):
                query, annotation = process(self.database, self.queryTextbox.toPlainText())
                self.queryOutput.setText('\n'.join(query))
                self.queryExplain.setText('\n'.join(annotation))
            except Exception as e:
                self.error_dialog.showMessage(f"ERROR - {e}")


def process(database,query):
    query_results=database.get_query_results(query)
    final_query_result=[]
    for i in query_results:
        temp=""
        for j in i:
            temp+=str(j)+"|"
        final_query_result.append(temp)
    raw_explanation=database.get_query_results("explain "+query)
    final_raw_explanation=[]
    for i in raw_explanation:
        final_raw_explanation.append(i[0])
    return final_query_result,final_raw_explanation
    #def init_conn(db_name):
    #    db_uname, db_pass, db_host, db_port = import_config()
    #    conn = open_db(db_name, db_uname, db_pass, db_host, db_port)
    #    return conn

    #def showError(self, errMessage, execption=None):
    #    dialog = QMessageBox()
    #    dialog.setStyleSheet("QLabel{min-width: 300px;}");
    #    dialog.setWindowTitle("Error")
    #    #dialog.setIcon(QMessageBox.Warning)
    #    dialog.setText(errMessage)
    #    if execption is not None:
    #        dialog.setDetailedText(str(execption))
    #    dialog.setStandardButtons(QMessageBox.Ok)
    #    #dialog.buttonClicked.connect(cb)
    #    dialog.exec_()


def window():
    app = QApplication(sys.argv)
    win = MyWindow()

    win.show()
    sys.exit(app.exec_())


def main():
    window()


if __name__ == '__main__':
    main()