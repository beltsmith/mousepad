/**
 * pin_length is measured along the same axis as the length of the board.
 * pin_width is the width of the bottom of the pins, where they're widest.
 * pin_thickness is measured along the same axis as the board thickness.
 * tail_width is the width of the tails where they meet the bottom of the pins, where they're narrowest.
 */
module dovetail_pins(pin_length=.75, pin_width=1, pin_thickness=.75, pin_count=4, angle=15, tail_width=1) {
    intersection() {
        translate([0.005, 0.005, 0.005]) cube([(pin_count-1)*(pin_width+tail_width)-0.01, pin_length-0.01, pin_thickness-0.01]);
        dovetail_pins_idealized(
            pin_length=pin_length,
            pin_width=pin_width,
            pin_thickness=pin_thickness,
            pin_count=pin_count,
            angle=angle,
            tail_width=tail_width
        );
    }
}

/**
 * tail_length is measured along the same axis as the length of the board.
 * tail_width is the width of the tails where they meet the board, where they're narrowest.
 * tail_thickness is measured along the same axis as the board thickness.
 * pin_width is the width of the bottom of the pins, where they're widest.
 */
module dovetail_tails(tail_length=.75, tail_width=1, tail_thickness=.75, tail_count=3, angle=15, pin_width=1) {
    pin_count = tail_count + 1;
    pin_thickness = tail_length;
    pin_length = tail_thickness;

    translate([0, 0, tail_thickness]) {
        rotate([-90, 0, 0]) {
            translate([0, 0, -0.005]) {
                difference() {
                    translate([0.005, 0.005, 0.005]) {
                        cube([(pin_count-1)*(pin_width+tail_width)-0.01, tail_thickness-0.01, tail_length-0.01]);
                    }

                    dovetail_pins_idealized(
                        pin_length=pin_length,
                        pin_width=pin_width,
                        pin_thickness=pin_thickness,
                        pin_count=pin_count,
                        angle=angle,
                        tail_width=tail_width
                    );
                }
            }
        }
    }
}

/**
 * Used for generating both pins and tails. Don't call this directly.
 */
module dovetail_pins_idealized(
    pin_length=.75,
    pin_width=1,
    pin_thickness=.75,
    pin_count=4,
    angle=15,
    tail_width=1) {

    pin_width_top = pin_width - ( 2 * tan(angle) * pin_thickness );

    translate([0, pin_length, 0]) {
        rotate([90, 0, 0]) {
            intersection() {
                cube([(pin_count-1)*(pin_width+tail_width), pin_thickness, pin_length]);

                for (pin = [0:pin_count-1]) {
                    translate([pin * (pin_width + tail_width), 0]) {
                        linear_extrude(pin_length) {
                            polygon([
                                [-pin_width/2, 0],
                                [-pin_width_top/2, pin_thickness],
                                [pin_width_top/2, pin_thickness],
                                [pin_width/2, 0]
                            ]);
                        }
                    }
                }
            }
        }
    }
}

/**
 * Determine the width the pins need to be once the tail widths are chosen.
 */
function pin_width (tail_width, tail_count, board_width) = ( board_width - (tail_width * tail_count) ) / (tail_count);

module platform(size) {
  width = size[0];
  diameter = size[1];
  height = size[2];
  platform_depth = size[3];

  radius = 20;
  y = diameter * 2;
  x = width / 2;

  translate([0,-platform_depth,0]) {
    linear_extrude(height=height)
      hull() {
      translate([radius - x, 0, 0])
        circle(r=radius);

      translate([x - radius, 0, 0])
        circle(r=radius);

      translate([radius - x, platform_depth, 0])
        circle(r=radius);

      translate([x - radius, platform_depth, 0])
        circle(r=radius);
    }

  }

  linear_extrude(height=height)
    hull() {
    // place 4 circles in the corners, with the given radius
    translate([radius - x, 0, 0])
      circle(r=radius);

    translate([x - radius, 0, 0])
      circle(r=radius);

    translate([0, y-(radius/2), 0])
      circle(r=radius);

  }
}

module hole(d, h) {
  cylinder(h=h*3, r = d/2, center = true);
}

platform_depth = 200;
platform_height = 20;
platform_width = 350;

mount_diameter = 30;
mount_radius = mount_diameter / 2;

difference() {
  platform([platform_width, mount_diameter, platform_height, platform_depth]);
  translate([0,mount_diameter/2, 0]) hole(mount_diameter, platform_height);
}
