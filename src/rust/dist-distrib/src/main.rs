use std::collections::HashMap;
use std::io;
use std::io::prelude::*;

const R: f64 = 6371.009;

fn distance(lat1: f64, long1: f64, lat2: f64, long2: f64) -> f64 {
    let lat1 = lat1.to_radians();
    let long1 = long1.to_radians();
    let lat2 = lat2.to_radians();
    let long2 = long2.to_radians();
    let delta_phi = lat2 - lat1;
    let delta_lambda = long2 - long1;
    let phi_m = (lat1 + lat2) / 2.0;
    R * (delta_phi.powi(2) + (phi_m.cos() * delta_lambda).powi(2)).sqrt()
}

fn main() {
    
    let mut distrib: HashMap<i32, i32> = HashMap::new();
    let stdin = io::stdin();
    
    for line in stdin.lock().lines() {
        let line = line.unwrap();
        let parts = line.split(" ").collect::<Vec<&str>>();

        let x1 = parts[2].parse::<f64>().unwrap();
        let y1 = parts[3].parse::<f64>().unwrap();
        let x2 = parts[5].parse::<f64>().unwrap();
        let y2 = parts[6].parse::<f64>().unwrap();

        // let dist : i32 = (r * (delta_phi.powi(2) + (phi_m.cos() * delta_lambda).powi(2)).sqrt()).ceil() as i32;
        let dist :i32 = distance(x1, y1, x2, y2).ceil() as i32;
        let distance_count = distrib.entry(dist).or_insert(0);
        *distance_count += 1;
        
        // println!("({}, {})\t({}, {})\t{}", x1, y1, x2, y2, dist);
    }
    // println!("{:?}", distrib);
    for (d, c) in &distrib {
        println!("{}\t{}", d, c);
    }
}
