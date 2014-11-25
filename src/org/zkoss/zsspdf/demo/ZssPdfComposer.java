/**
 * 
 */
package org.zkoss.zsspdf.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zss.api.Exporter;
import org.zkoss.zss.api.Exporters;
import org.zkoss.zss.api.Importer;
import org.zkoss.zss.api.Importers;
import org.zkoss.zss.api.model.Book;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;

/**
 * @author ashish
 *
 */
public class ZssPdfComposer extends GenericForwardComposer {
	
	private static final long serialVersionUID = 1L;
	
	
	private transient Checkbox printHeadings;
	private transient Iframe report;
	private transient Radio openFile;
	private transient Radio saveFile;
	private transient Radio  saveAsExcelFile;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		openFile.setChecked(true);
	}
	
	public void onUpload$xlsUploadBtn(UploadEvent event)
			throws IOException, InterruptedException {

		org.zkoss.util.media.Media media = event.getMedia();
		if (media.isBinary()) {

			Importer importer = Importers.getImporter("excel");
			Book wb = importer.imports(media.getStreamData(), media.getName());

			Exporter c = Exporters.getExporter("pdf");
//			((PdfExporter) c).enableHeadings(printHeadings.isChecked());

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			c.export(wb, baos);
//			c.exportActiveSheet(wb.getSheetAt(wb.getActiveSheetIndex()), os);
//			c.exportSelection(wb.getSheetAt(wb.getActiveSheetIndex()), new AreaReference("A8:I18"), os);

			final AMedia amedia = new AMedia("sample", "pdf",
					"application/pdf", baos.toByteArray());

			if(openFile.isChecked()) {
				report.setContent(amedia);
			} else if(saveFile.isChecked()) {
				Filedownload.save(amedia);
			}
		} else {
			Messagebox.show("Not a binary file: " + media, "Error",
					Messagebox.OK, Messagebox.ERROR);
		}
	}
}
