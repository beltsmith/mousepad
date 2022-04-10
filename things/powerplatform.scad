union () {
  difference () {
    translate ([0, 0, -8]) {
      hull () {
        translate ([-30, -45, 0]) {
          cylinder (h=13, r=15);
        }
        linear_extrude (height=13){
          hull () {
            translate ([-333/2, 10, 0]) {
              circle (r=10);
            }
            translate ([333/2, 10, 0]) {
              circle (r=10);
            }
            translate ([333/2, 285, 0]) {
              circle (r=10);
            }
            translate ([-333/2, 285, 0]) {
              circle (r=10);
            }
          }
        }
        translate ([213/2, -36, 0]) {
          linear_extrude (height=13){
            hull () {
              translate ([-60, 10, 0]) {
                circle (r=10);
              }
              translate ([60, 10, 0]) {
                circle (r=10);
              }
              translate ([60, 30, 0]) {
                circle (r=10);
              }
              translate ([-60, 30, 0]) {
                circle (r=10);
              }
            }
          }
        }
      }
    }
    union () {
      translate ([0, 5, 0]) {
        linear_extrude (height=5){
          hull () {
            translate ([-323/2, 10, 0]) {
              circle (r=10);
            }
            translate ([323/2, 10, 0]) {
              circle (r=10);
            }
            translate ([323/2, 275, 0]) {
              circle (r=10);
            }
            translate ([-323/2, 275, 0]) {
              circle (r=10);
            }
          }
        }
      }
      translate ([213/2, -36, 0]) {
        translate ([0, -7, 0]) {
          linear_extrude (height=13){
            hull () {
              translate ([-55, 10, 0]) {
                circle (r=10);
              }
              translate ([55, 10, 0]) {
                circle (r=10);
              }
              translate ([55, 275, 0]) {
                circle (r=10);
              }
              translate ([-55, 275, 0]) {
                circle (r=10);
              }
            }
          }
        }
      }
    }
    translate ([0, 0, -8]) {
      translate ([0, -25, 0]) {
        cylinder (h=27, r=15);
      }
    }
  }
  translate ([0, -25, -8]) {
    difference () {
      cylinder (h=27, r=17);
      cylinder (h=27, r=15);
    }
  }
}
