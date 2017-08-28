/*
 * (C) Copyright 2015-2016 by MSDK Development Team
 *
 * This software is dual-licensed under either
 *
 * (a) the terms of the GNU Lesser General Public License version 2.1 as published by the Free
 * Software Foundation
 *
 * or (per the licensee's choosing)
 *
 * (b) the terms of the Eclipse Public License v1.0 as published by the Eclipse Foundation.
 */

package io.github.msdk.io.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import io.github.msdk.MSDKException;
import io.github.msdk.datamodel.chromatograms.Chromatogram;
import io.github.msdk.datamodel.chromatograms.ChromatogramType;
import io.github.msdk.datamodel.msspectra.MsSpectrumType;
import io.github.msdk.datamodel.rawdata.MsScan;
import io.github.msdk.datamodel.rawdata.PolarityType;
import io.github.msdk.datamodel.rawdata.RawDataFile;
import io.github.msdk.io.mzml.MzMLFileExportMethod;
import io.github.msdk.io.mzml.MzMLFileImportMethod;
import io.github.msdk.io.mzml.data.MzMLCompressionType;
import io.github.msdk.util.MsSpectrumUtil;

public class MzMLTests {

  private static final String TEST_DATA_PATH = "src/test/resources/mzML/";

  @Test
  public void testK3HighPos() throws MSDKException, IOException {

    float intensityBuffer[];

    // Import the file
    File inputFile = new File(TEST_DATA_PATH + "046_CRa_H9M5_M470_H03_K3_high_pos.mzML");
    Assert.assertTrue(inputFile.canRead());
    MzMLFileImportMethod importer = new MzMLFileImportMethod(inputFile, s -> false, c -> false);
    RawDataFile rawFile = importer.execute();
    Assert.assertNotNull(rawFile);
    Assert.assertEquals(1.0, importer.getFinishedPercentage(), 0.0001);

    // Export the file to a new mzML
    File tempFile = File.createTempFile("msdk", ".mzML");
    MzMLFileExportMethod exporter = new MzMLFileExportMethod(rawFile, tempFile,
        MzMLCompressionType.NUMPRESS_LINPRED_ZLIB, MzMLCompressionType.ZLIB);
    exporter.execute();
    Assert.assertEquals(1.0, exporter.getFinishedPercentage(), 0.0001);

    // Import the new mzML
    importer = new MzMLFileImportMethod(tempFile);
    RawDataFile newMzMLFile = importer.execute();
    Assert.assertNotNull(newMzMLFile);
    Assert.assertEquals(1.0, importer.getFinishedPercentage(), 0.0001);

    // Check number of scans
    List<MsScan> scans = newMzMLFile.getScans();
    Assert.assertNotNull(scans);
    Assert.assertEquals(rawFile.getScans().size(), scans.size());

    // 2nd scan, #2
    MsScan scan2 = scans.get(1);
    Assert.assertEquals(new Integer(2), scan2.getScanNumber());
    Assert.assertEquals(MsSpectrumType.PROFILE, scan2.getSpectrumType());
    Assert.assertEquals(new Integer(1), scan2.getMsLevel());
    Assert.assertEquals(0.617502f, scan2.getRetentionTime(), 0.01f);
    Assert.assertEquals(PolarityType.POSITIVE, scan2.getPolarity());
    intensityBuffer = scan2.getIntensityValues();
    Assert.assertEquals(3371, (int) scan2.getNumberOfDataPoints());
    Float scan2maxInt =
        MsSpectrumUtil.getMaxIntensity(intensityBuffer, scan2.getNumberOfDataPoints());
    Assert.assertEquals(1.638E4f, scan2maxInt, 1E4f);

    // The file has 1 chromatogram
    List<Chromatogram> chromatograms = newMzMLFile.getChromatograms();
    Assert.assertNotNull(chromatograms);
    Assert.assertEquals(1, chromatograms.size());

    // 1st Chromatogram, #1
    Chromatogram chromatogram = chromatograms.get(0);
    Assert.assertEquals(Integer.valueOf(1), chromatogram.getChromatogramNumber());
    Assert.assertEquals(ChromatogramType.TIC, chromatogram.getChromatogramType());
    Assert.assertEquals(Integer.valueOf(1620), chromatogram.getNumberOfDataPoints());
    Assert.assertEquals(Integer.valueOf(0), (Integer) chromatogram.getIsolations().size());
    Assert.assertEquals(0.00224596, chromatogram.getRetentionTimes()[0], 0.0001);
    Assert.assertEquals(276281.0, chromatogram.getIntensityValues()[0], 0.0001);

    // Cleanup
    rawFile.dispose();
    newMzMLFile.dispose();
    tempFile.delete();

  }

  @Test
  public void testLPGiRPPos() throws MSDKException, IOException {

    float intensityBuffer[];

    // Import the file
    File inputFile = new File(TEST_DATA_PATH + "L045_036_LPGi_RP_pos.mzML");
    Assert.assertTrue(inputFile.canRead());
    MzMLFileImportMethod importer = new MzMLFileImportMethod(inputFile, s -> false, c -> false);
    RawDataFile rawFile = importer.execute();
    Assert.assertNotNull(rawFile);
    Assert.assertEquals(1.0, importer.getFinishedPercentage(), 0.0001);

    // Check number of scans
    List<MsScan> scans = rawFile.getScans();
    Assert.assertNotNull(scans);
    Assert.assertEquals(2441, scans.size());

    // 79th scan, #79
    MsScan scan79 = scans.get(78);
    Assert.assertEquals(new Integer(79), scan79.getScanNumber());
    Assert.assertEquals(MsSpectrumType.CENTROIDED, scan79.getSpectrumType());
    Assert.assertEquals(new Integer(1), scan79.getMsLevel());
    Assert.assertEquals(57.48600126f, scan79.getRetentionTime(), 0.01f);
    Assert.assertEquals(PolarityType.POSITIVE, scan79.getPolarity());
    intensityBuffer = scan79.getIntensityValues();
    Assert.assertEquals(634, (int) scan79.getNumberOfDataPoints());
    Float scan79maxInt =
        MsSpectrumUtil.getMaxIntensity(intensityBuffer, scan79.getNumberOfDataPoints());
    Assert.assertEquals(8f, scan79maxInt, 1E01f);

    // 362nd scan, #362
    MsScan scan362 = scans.get(361);
    Assert.assertEquals(new Integer(362), scan362.getScanNumber());
    Assert.assertEquals(MsSpectrumType.CENTROIDED, scan362.getSpectrumType());
    Assert.assertEquals(new Integer(1), scan362.getMsLevel());
    Assert.assertEquals(259.8760128f, scan362.getRetentionTime(), 0.01f);
    Assert.assertEquals(PolarityType.POSITIVE, scan362.getPolarity());
    intensityBuffer = scan362.getIntensityValues();
    Assert.assertEquals(2506, (int) scan362.getNumberOfDataPoints());
    Float scan362maxInt =
        MsSpectrumUtil.getMaxIntensity(intensityBuffer, scan362.getNumberOfDataPoints());
    Assert.assertEquals(673f, scan362maxInt, 1E01f);

    // The file has 1 chromatogram
    List<Chromatogram> chromatograms = rawFile.getChromatograms();
    Assert.assertNotNull(chromatograms);
    Assert.assertEquals(1, chromatograms.size());

    // 1st Chromatogram, #1
    Chromatogram chromatogram = chromatograms.get(0);
    Assert.assertEquals(Integer.valueOf(1), chromatogram.getChromatogramNumber());
    Assert.assertEquals(ChromatogramType.TIC, chromatogram.getChromatogramType());
    Assert.assertEquals(Integer.valueOf(2441), chromatogram.getNumberOfDataPoints());
    Assert.assertEquals(Integer.valueOf(0), (Integer) chromatogram.getIsolations().size());
    Assert.assertEquals(0.0, chromatogram.getRetentionTimes()[0], 0.0001);
    Assert.assertEquals(0.0, chromatogram.getIntensityValues()[0], 0.0001);

    // Cleanup
    rawFile.dispose();

  }

  @Test
  public void testBSA2() throws MSDKException, IOException {

    float intensityBuffer[];

    // Import the file
    File inputFile = new File(TEST_DATA_PATH + "BSA2.mzML");
    Assert.assertTrue(inputFile.canRead());
    FileInputStream fis = new FileInputStream(inputFile);
    MzMLFileImportMethod importer =
        new MzMLFileImportMethod(fis, s -> s.getScanNumber() % 700 == 0, c -> false);
    RawDataFile rawFile = importer.execute();
    Assert.assertNotNull(rawFile);
    Assert.assertEquals(1.0, importer.getFinishedPercentage(), 0.0001);

    // Check number of scans
    List<MsScan> scans = rawFile.getScans();
    Assert.assertNotNull(scans);
    Assert.assertEquals(2, scans.size());

    // 1st scan, #700
    MsScan scan1 = scans.get(0);
    Assert.assertEquals(new Integer(700), scan1.getScanNumber());
    Assert.assertEquals(MsSpectrumType.CENTROIDED, scan1.getSpectrumType());
    Assert.assertEquals(new Integer(2), scan1.getMsLevel());
    Assert.assertEquals(1726.64953613281f, scan1.getRetentionTime(), 0.01f);
    Assert.assertEquals(PolarityType.POSITIVE, scan1.getPolarity());
    intensityBuffer = scan1.getIntensityValues();
    Assert.assertEquals(88, (int) scan1.getNumberOfDataPoints());
    Float scan79maxInt =
        MsSpectrumUtil.getMaxIntensity(intensityBuffer, scan1.getNumberOfDataPoints());
    Assert.assertEquals(132.43391418457f, scan79maxInt, 1E01f);

    // 2nd scan, #1400
    MsScan scan2 = scans.get(1);
    Assert.assertEquals(new Integer(1400), scan2.getScanNumber());
    Assert.assertEquals(MsSpectrumType.CENTROIDED, scan2.getSpectrumType());
    Assert.assertEquals(new Integer(2), scan2.getMsLevel());
    Assert.assertEquals(2243.19213867188f, scan2.getRetentionTime(), 0.01f);
    Assert.assertEquals(PolarityType.POSITIVE, scan2.getPolarity());
    intensityBuffer = scan2.getIntensityValues();
    Assert.assertEquals(20, (int) scan2.getNumberOfDataPoints());
    Float scan2maxInt =
        MsSpectrumUtil.getMaxIntensity(intensityBuffer, scan2.getNumberOfDataPoints());
    Assert.assertEquals(146.620513916016f, scan2maxInt, 1E01f);

    // The file has no chromatograms
    List<Chromatogram> chromatograms = rawFile.getChromatograms();
    Assert.assertNotNull(chromatograms);
    Assert.assertEquals(0, chromatograms.size());

    // Cleanup
    rawFile.dispose();

  }

  @Test
  public void testTargetedPosLipid2() throws MSDKException, IOException {

    // Import the file
    File inputFile =
        new File(TEST_DATA_PATH + "targeted_positive_lipid2_20121114_Lipdi_2_H1_85.mzML");
    Assert.assertTrue(inputFile.canRead());
    FileInputStream fis = new FileInputStream(inputFile);
    MzMLFileImportMethod importer = new MzMLFileImportMethod(fis, s -> false, c -> false);
    RawDataFile rawFile = importer.execute();
    Assert.assertNotNull(rawFile);
    Assert.assertEquals(1.0, importer.getFinishedPercentage(), 0.0001);

    // Check number of scans
    List<MsScan> scans = rawFile.getScans();
    Assert.assertNotNull(scans);
    Assert.assertEquals(0, scans.size());

    // The file has no chromatogram
    List<Chromatogram> chromatograms = rawFile.getChromatograms();
    Assert.assertNotNull(chromatograms);
    Assert.assertEquals(0, chromatograms.size());

    // Cleanup
    rawFile.dispose();

  }

}
