use std::collections::HashMap;
use std::io;
use std::io::prelude::*;

fn main() {
    let r = 6371.009;
    let mut distrib: HashMap<i32, i32> = HashMap::new();
    let stdin = io::stdin();
    
    for line in stdin.lock().lines() {
        let line = line.unwrap();
        let parts = line.split(" ").collect::<Vec<&str>>();

        let x1 = parts[2].parse::<f32>().unwrap();
        let y1 = parts[3].parse::<f32>().unwrap();
        let x2 = parts[5].parse::<f32>().unwrap();
        let y2 = parts[6].parse::<f32>().unwrap();

        let delta_phi = x1 - x2;
        let delta_lambda = y1 - y2;
        let phi_m = (x1 + x2) / 2f32;

        let dist : i32 = (r * (delta_phi.powi(2) + (phi_m.cos() * delta_lambda).powi(2)).sqrt()).ceil() as i32;
        let distance_count = distrib.entry(dist).or_insert(0);
        *distance_count += 1;
        
        // println!("({}, {})\t({}, {})\t{}", x1, y1, x2, y2, dist);
    }
    // println!("{:?}", distrib);
    for (d, c) in &distrib {
        println!("{}\t{}", d, c);
    }
}
