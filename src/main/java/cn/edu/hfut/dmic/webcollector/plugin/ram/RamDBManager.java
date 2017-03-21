/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.edu.hfut.dmic.webcollector.plugin.ram;

import cn.edu.hfut.dmic.webcollector.crawldb.DBManager;
import cn.edu.hfut.dmic.webcollector.crawldb.Generator;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hu
 */
public class RamDBManager extends DBManager {

    Logger LOG = LoggerFactory.getLogger(DBManager.class);

    public RamDB ramDB;
    public RamGenerator generator=null;

    public RamDBManager(RamDB ramDB) {
        this.ramDB = ramDB;
        this.generator=new RamGenerator(ramDB);
    }

    
    public boolean isDBExists() {
        return true;
    }

    
    public void clear() throws Exception {
        ramDB.crawlDB.clear();
        ramDB.fetchDB.clear();
        ramDB.linkDB.clear();
        ramDB.redirectDB.clear();
    }

    
    public Generator getGenerator() {
        return generator;
    }

    
    public void open() throws Exception {
    }

    
    public void close() throws Exception {
    }

    
    public void inject(CrawlDatum datum, boolean force) throws Exception {
        String key = datum.getKey();
        if (!force) {
            if (ramDB.crawlDB.containsKey(key)) {
                return;
            }
        }
        ramDB.crawlDB.put(key, datum);
    }

    
    public void merge() throws Exception {
        LOG.info("start merge");

        /*合并fetch库*/
        LOG.info("merge fetch database");
        for (Entry<String, CrawlDatum> fetchEntry : ramDB.fetchDB.entrySet()) {
            ramDB.crawlDB.put(fetchEntry.getKey(), fetchEntry.getValue());
        }

        /*合并link库*/
        LOG.info("merge link database");
        for (String key : ramDB.linkDB.keySet()) {
            if (!ramDB.crawlDB.containsKey(key)) {
                ramDB.crawlDB.put(key, ramDB.linkDB.get(key));
            }
        }

        LOG.info("end merge");

        ramDB.fetchDB.clear();
        LOG.debug("remove fetch database");
        ramDB.linkDB.clear();
        LOG.debug("remove link database");

    }

    
    public void initSegmentWriter() throws Exception {
    }

    
    public void wrtieFetchSegment(CrawlDatum fetchDatum) throws Exception {
        ramDB.fetchDB.put(fetchDatum.getKey(), fetchDatum);
    }

    
    public void wrtieParseSegment(CrawlDatums parseDatums) throws Exception {
        for (CrawlDatum datum : parseDatums) {
            ramDB.linkDB.put(datum.getKey(), datum);
        }
    }

    
    public void closeSegmentWriter() throws Exception {
    }

    
    public void lock() throws Exception {
    }

    
    public boolean isLocked() throws Exception {
        return false;
    }

    
    public void unlock() throws Exception {
    }

    
    public void writeRedirectSegment(CrawlDatum datum, String realUrl) throws Exception {
        String key = datum.getKey();
        ramDB.redirectDB.put(key, realUrl);
    }

}
